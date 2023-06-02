package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.enums.SortBy;
import com.atlantbh.auctionappbackend.exception.AppUserNotFoundException;
import com.atlantbh.auctionappbackend.exception.CategoryNotFoundException;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.model.*;
import com.atlantbh.auctionappbackend.repository.*;
import com.atlantbh.auctionappbackend.request.CsvProductRequest;
import com.atlantbh.auctionappbackend.request.NewProductRequest;
import com.atlantbh.auctionappbackend.response.AppUserProductsResponse;
import com.atlantbh.auctionappbackend.response.HighlightedProductResponse;
import com.atlantbh.auctionappbackend.response.ProductsResponse;
import com.atlantbh.auctionappbackend.response.SingleProductResponse;
import com.atlantbh.auctionappbackend.utils.ProductSpecifications;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Reader;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.atlantbh.auctionappbackend.utils.Constants.S3_KEY_PREFIX;
import static com.atlantbh.auctionappbackend.utils.Constants.SEARCH_VALIDATOR;
import static com.atlantbh.auctionappbackend.utils.LevenshteinDistanceCalculation.calculate;
import static java.lang.Long.parseLong;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final TokenService tokenService;

    private final AppUserRepository appUserRepository;

    private final CategoryRepository categoryRepository;

    private final SubcategoryRepository subcategoryRepository;

    private final ImageRepository imageRepository;

    private final S3Service s3Service;

    private final BidRepository bidRepository;

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public String getSuggestion(String query) {
        if (!query.matches(SEARCH_VALIDATOR) || query.isBlank()) {
            return "No suggestion found";
        }

        List<String> suggestions = productRepository.findTopNamesByNameSimilarity(query);

        if (suggestions.isEmpty()) {
            return "No suggestion found";
        } else {
            String bestSuggestion = null;
            int bestWeight = Integer.MAX_VALUE;

            for (String suggestion : suggestions) {
                int weight = calculate(query.toLowerCase(), suggestion.toLowerCase());
                if (weight < bestWeight) {
                    bestSuggestion = suggestion;
                    bestWeight = weight;
                }
            }

            return bestSuggestion != null ? bestSuggestion : "No suggestion found";
        }
    }

    public List<AppUserProductsResponse> retrieveUserProductsByType(Long userId, SortBy sortingType) {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneOffset.UTC);
        List<Product> userProducts;

        if (sortingType == SortBy.SOLD) {
            userProducts = productRepository.findAllByUserIdAndEndDateBeforeAndSoldIsTrue(
                    userId, currentTime, Sort.by(Sort.Direction.DESC, SortBy.END_DATE.getSort()));
        } else {
            userProducts = productRepository.findAllByUserIdAndEndDateAfterAndSoldIsFalse(
                    userId, currentTime, Sort.by(Sort.Direction.DESC, SortBy.START_DATE.getSort()));
        }

        return userProducts.stream().map(product -> new AppUserProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0).getImageUrl(), product.getEndDate(), product.getNumberOfBids(), product.getHighestBid())).toList();
    }

    public List<ProductsResponse> getRecommendedProducts(Long userId) {
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<Product> recommendedProducts = productRepository.findRecommendedProducts(userId, pageRequest);

        if (recommendedProducts.isEmpty()) {
            recommendedProducts = productRepository.findFirstActiveProducts(pageRequest);
        }

        return recommendedProducts.stream()
                .map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0).getImageUrl(), product.getCategory().getId()))
                .toList();
    }


    public Page<ProductsResponse> getAllFilteredProducts(int pageNumber, int pageSize, String searchTerm, Long categoryId, SortBy sortBy) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if(!searchTerm.isBlank()) {
            specification = Specification.where(ProductSpecifications.hasNameLike(searchTerm));
        }

        if (categoryId != null) {
            Specification<Product> categorySpec = ProductSpecifications.hasCategoryId(categoryId);
            specification = specification.and(categorySpec);
        }

        Sort sort = switch (sortBy) {
            case PRICE_HIGH_TO_LOW -> Sort.by(Sort.Direction.DESC, "startPrice");
            case PRICE_LOW_TO_HIGH -> Sort.by(Sort.Direction.ASC, "startPrice");
            case END_DATE -> Sort.by(Sort.Direction.ASC, "endDate");
            case START_DATE -> Sort.by(Sort.Direction.DESC, "startDate");
            case DEFAULT_SORTING, default -> Sort.unsorted();
        };

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> products;
        try {
            products = productRepository.findAll(specification, pageable);
        } catch (Exception e) {
            log.error("Error while fetching products from the database", e);
            throw new ProductNotFoundException("Searched products don't exist");
        }
        return products.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0).getImageUrl(), product.getCategory().getId()));
    }

    public void createProduct(NewProductRequest request, List<MultipartFile> images, HttpServletRequest httpServletRequest) {
        String token = tokenService.getJwtFromCookie(httpServletRequest);
        String email = tokenService.getClaimFromToken(token, "sub");

        Optional<AppUser> userOpt = appUserRepository.getByEmail(email);
        if (userOpt.isEmpty()) {
            throw new AppUserNotFoundException("User not found!");
        }
        AppUser appUser = userOpt.get();
        Product product = Product.builder()
                .productName(request.getProductName())
                .description(request.getDescription())
                .startPrice(Float.parseFloat(request.getStartPrice()))
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .city(request.getCity())
                .zipCode(request.getZipCode())
                .country(request.getCountry())
                .phone(request.getPhone())
                .user(appUser)
                .build();

        Category category = categoryRepository.findById(parseLong(request.getCategoryId())).orElseThrow(() -> new CategoryNotFoundException("Category not found."));
        product.setCategory(category);

        Subcategory subcategory = subcategoryRepository.findById(parseLong(request.getCategoryId())).orElseThrow(() -> new CategoryNotFoundException("Category not found."));
        product.setSubcategory(subcategory);

        Product savedProduct = productRepository.save(product);

        for (MultipartFile image : images) {
            String imageUrl = s3Service.uploadFile(image, S3_KEY_PREFIX + savedProduct.getId() + "/");
            Image img = new Image();
            img.setImageUrl(imageUrl);
            img.setProduct(savedProduct);
            imageRepository.save(img);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void processCsvFileToCreateProduct(Reader reader) throws Exception {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        String email = principal.getName();
        Optional<AppUser> userOpt = appUserRepository.getByEmail(email);
        CsvToBean<CsvProductRequest> csvToBean = new CsvToBeanBuilder<CsvProductRequest>(reader)
                .withType(CsvProductRequest.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        AppUser user = userOpt.orElseThrow(() -> new AppUserNotFoundException("User not found"));

        List<CsvProductRequest> products = csvToBean.parse();
        
            for (CsvProductRequest product : products) {
                validateProductBeforeStoring(product);

                Optional<Category> categoryOpt = categoryRepository.findByCategoryName(product.getCategoryName());
                Optional<Subcategory> subCategoryOpt = subcategoryRepository.findBySubcategoryName(product.getCategoryName());

                if (!categoryOpt.isPresent() || !subCategoryOpt.isPresent()) {
                    throw new CategoryNotFoundException("Projected category or subcategory does not exist");
                }

                Product fullProduct = new Product(
                        product.getProductName(),
                        product.getDescription(),
                        Float.parseFloat(product.getStartPrice()),
                        product.getImages(),
                        product.getStartDate(),
                        product.getEndDate(),
                        categoryOpt.get(),
                        subCategoryOpt.get(),
                        user,
                        product.getAddress(),
                        product.getCity(),
                        product.getZipCode(),
                        product.getCountry(),
                        product.getPhone()
                );
                productRepository.save(fullProduct);
            }
    }

    public Page<ProductsResponse> getNewProducts(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Product> products = productRepository.getNewArrivalsProducts(pageable);
        return products.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0).getImageUrl(), product.getCategory().getId()));
    }


    public SingleProductResponse getProductById(Long id) throws ProductNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        boolean isOwner = false;
        float userHighestBid = 0f;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            isOwner = product.isOwner(email);

            Optional<AppUser> userOpt = appUserRepository.getByEmail(email);
            if (userOpt.isPresent()) {
                Optional<Float> highestBidOpt = bidRepository.getMaxBidFromUserForProduct(userOpt.get().getId(), id);
                userHighestBid = highestBidOpt.orElse(0f);
            }
        }

        return new SingleProductResponse(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getStartPrice(),
                product.getImages().stream().map(Image::getImageUrl).collect(Collectors.toList()),
                product.getStartDate(),
                product.getEndDate(),
                product.getNumberOfBids(),
                product.getHighestBid(),
                product.getUser().getId(),
                isOwner,
                product.isSold(),
                userHighestBid
        );
    }

    public Page<ProductsResponse> getLastProducts(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Product> products = productRepository.getLastChanceProducts(pageable);
        return products.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0).getImageUrl(), product.getCategory().getId()));
    }

    public List<HighlightedProductResponse> getHighlightedProducts() {
        List<Product> products = productRepository.findByIsHighlightedTrue();
        return products.stream()
                .map(product -> new HighlightedProductResponse(
                        product.getId(),
                        product.getProductName(),
                        product.getStartPrice(),
                        product.getImages().get(0).getImageUrl(),
                        product.getDescription())).toList();
    }

    private void validateProductBeforeStoring(CsvProductRequest productRequest) throws Exception {

        if (productRequest.getProductName().isEmpty() || productRequest.getDescription().isEmpty()) {
            throw new Exception("Please enter a name for the product and a relevant description.");
        }

        if (productRequest.getStartDate().isAfter(productRequest.getEndDate())) {
            throw new Exception("Start date cannot be after end date.");
        }

        if (productRequest.getEndDate().isBefore(productRequest.getStartDate())) {
            throw new Exception("End date cannot be before start date.");
        }

        if (Float.parseFloat(productRequest.getStartPrice()) < 0) {
            throw new Exception("Start price cannot be less than 0.");
        }

        if (productRequest.getImages().isEmpty() || productRequest.getImages().size() < 3) {
            throw new Exception("Product must have at least 3 images.");
        }

        if (productRequest.getAddress().isEmpty() ||
                productRequest.getCity().isEmpty() ||
                productRequest.getZipCode().isEmpty() ||
                productRequest.getCountry().isEmpty() ||
                productRequest.getPhone().isEmpty()) {
            throw new Exception("All shipment details must be provided.");
        }
    }
}
