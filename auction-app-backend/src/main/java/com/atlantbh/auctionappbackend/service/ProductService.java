package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.dto.ProductCsvImport;
import com.atlantbh.auctionappbackend.dto.UserMaxBidRecord;
import com.atlantbh.auctionappbackend.enums.SortBy;
import com.atlantbh.auctionappbackend.exception.CategoryNotFoundException;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.exception.SubcategoryNotFoundException;
import com.atlantbh.auctionappbackend.model.*;
import com.atlantbh.auctionappbackend.repository.*;
import com.atlantbh.auctionappbackend.request.NewProductRequest;
import com.atlantbh.auctionappbackend.response.AppUserProductsResponse;
import com.atlantbh.auctionappbackend.response.HighlightedProductResponse;
import com.atlantbh.auctionappbackend.response.ProductsResponse;
import com.atlantbh.auctionappbackend.response.SingleProductResponse;
import com.atlantbh.auctionappbackend.utils.ByteToMultipartFileConverter;
import com.atlantbh.auctionappbackend.utils.CsvToBeanParser;
import com.atlantbh.auctionappbackend.utils.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Reader;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.atlantbh.auctionappbackend.utils.Constants.S3_KEY_PREFIX;
import static com.atlantbh.auctionappbackend.utils.Constants.SEARCH_VALIDATOR;
import static com.atlantbh.auctionappbackend.utils.LevenshteinDistanceCalculation.calculate;
import static java.lang.Float.parseFloat;


@Service
@RequiredArgsConstructor
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final TokenService tokenService;

    private final CategoryRepository categoryRepository;

    private final SubcategoryRepository subcategoryRepository;

    private final ImageRepository imageRepository;

    private final S3Service s3Service;

    private final BidRepository bidRepository;

    private final RestTemplate restTemplate;

    private final CsvToBeanParser<ProductCsvImport> parser;

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

    public List<ProductsResponse> getRecommendedProducts(HttpServletRequest request) {
        AppUser appUser = tokenService.getAuthenticatedUser(request);

        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<Product> recommendedProducts = productRepository.findRecommendedProducts(appUser.getId(), pageRequest);

        if (recommendedProducts.isEmpty()) {
            recommendedProducts = productRepository.findFirstActiveProducts(pageRequest);
        }

        return recommendedProducts.stream()
                .map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0).getImageUrl(), product.getCategory().getId()))
                .toList();
    }


    public Page<ProductsResponse> getAllFilteredProducts(int pageNumber, int pageSize, String searchTerm, Long categoryId, SortBy sortBy) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (!searchTerm.isBlank()) {
            specification = Specification.where(ProductSpecifications.hasNameLike(searchTerm));
        }

        if (categoryId != null && categoryId != 10) {
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
        Page<Product> products = productRepository.findAll(specification, pageable);

        return products.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0).getImageUrl(), product.getCategory().getId()));
    }

    public void createProduct(NewProductRequest request, List<MultipartFile> images, HttpServletRequest httpServletRequest) {
        AppUser user = tokenService.getAuthenticatedUser(httpServletRequest);

        ShippingInfo shippingInfo = ShippingInfo.builder()
                .city(request.getCity())
                .address(request.getAddress())
                .country(request.getCountry())
                .zipCode(request.getZipCode())
                .phone(request.getPhone())
                .build();

        Product product = Product.builder()
                .productName(request.getProductName())
                .description(request.getDescription())
                .startPrice(request.getStartPrice())
                .startDate(request.getStartDate().withZoneSameInstant(ZoneOffset.UTC))
                .endDate(request.getEndDate().withZoneSameInstant(ZoneOffset.UTC))
                .info(shippingInfo)
                .user(user)
                .build();

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException("Category not found."));
        product.setCategory(category);

        Subcategory subcategory = subcategoryRepository.findById(request.getSubcategoryId()).orElseThrow(() -> new SubcategoryNotFoundException("Subcategory not found."));
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
    public void processCsvFileToCreateProducts(Reader reader, HttpServletRequest request) throws Exception {
        AppUser user = tokenService.getAuthenticatedUser(request);

        List<ProductCsvImport> productsFromCsvFile = parser.parse(reader, ProductCsvImport.class);

        for (ProductCsvImport product : productsFromCsvFile) {
            processSingleProductToCreateIt(product, user);
        }
    }

    public Page<ProductsResponse> getNewProducts(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Product> products = productRepository.getNewArrivalsProducts(pageable);

        if (products.isEmpty()) {
            return Page.empty();
        }
        return products.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0).getImageUrl(), product.getCategory().getId()));
    }

    public SingleProductResponse getProductById(Long productId, HttpServletRequest request) throws ProductNotFoundException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        SingleProductResponse.SingleProductResponseBuilder responseBuilder = SingleProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .startPrice(product.getStartPrice())
                .images(product.getImages().stream().map(Image::getImageUrl).collect(Collectors.toList()))
                .startDate(product.getStartDate())
                .endDate(product.getEndDate())
                .numberOfBids(product.getNumberOfBids())
                .highestBid(product.getHighestBid())
                .userId(product.getUser().getId())
                .isOwner(false)
                .sold(product.isSold());

        AppUser user = tokenService.getAuthenticatedUser(request);
        boolean isOwner = product.isOwner(user.getId());
        responseBuilder.isOwner(isOwner);
        PageRequest page = PageRequest.of(0, 1);

        List<UserMaxBidRecord> userWithHighestBid = bidRepository.findHighestBidAndUserByProduct(productId, page);

        Long highestBidUserId = null;

        if (!userWithHighestBid.isEmpty()) {
            highestBidUserId = userWithHighestBid.get(0).getUserId();
        }

        responseBuilder.isUserHighestBidder(highestBidUserId != null && highestBidUserId.equals(user.getId()));

        return responseBuilder.build();
    }

    public Page<ProductsResponse> getLastProducts(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Product> products = productRepository.getLastChanceProducts(pageable);

        if (products.isEmpty()) {
            return Page.empty();
        }
        return products.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0).getImageUrl(), product.getCategory().getId()));
    }

    public List<HighlightedProductResponse> getHighlightedProducts() {
        List<Product> products = productRepository.findByIsHighlightedTrue();

        if (products.isEmpty()) {
            return new ArrayList<>();
        }
        return products.stream()
                .map(product -> new HighlightedProductResponse(
                        product.getId(),
                        product.getProductName(),
                        product.getStartPrice(),
                        product.getImages().get(0).getImageUrl(),
                        product.getDescription())).toList();
    }

    private void validateProductBeforeStoring(ProductCsvImport product) throws Exception {

        if (product.getProductName().isBlank() || product.getDescription().isBlank()) {
            throw new Exception("Please enter a name for the product and a relevant description.");
        }

        if (ZonedDateTime.parse(product.getStartDate()).isAfter(ZonedDateTime.parse(product.getEndDate()))) {
            throw new Exception("Start date cannot be after end date.");
        }

        if (parseFloat(product.getStartPrice()) < 0) {
            throw new Exception("Start price cannot be less than 0.");
        }

        if (product.getImages().isEmpty() || product.getImages().size() < 3) {
            throw new Exception("Product must have at least 3 images.");
        }

        if (product.getAddress().isBlank() ||
                product.getCity().isBlank() ||
                product.getZipCode().isBlank() ||
                product.getCountry().isBlank() ||
                product.getPhone().isBlank()) {
            throw new Exception("All shipment details must be provided.");
        }
    }

    private void processSingleProductToCreateIt(ProductCsvImport product, AppUser user) throws Exception {

        validateProductBeforeStoring(product);

        Category category = categoryRepository.findByCategoryName(product.getCategoryName())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        Subcategory subcategory = subcategoryRepository.findBysubCategoryName(product.getSubcategoryName())
                .orElseThrow(() -> new SubcategoryNotFoundException("Subcategory not found"));

        ZonedDateTime startDate = ZonedDateTime.parse(product.getStartDate()).withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime endDate = ZonedDateTime.parse(product.getEndDate()).withZoneSameInstant(ZoneOffset.UTC);

        ShippingInfo shippingInfo = ShippingInfo.builder()
                .city(product.getCity())
                .address(product.getAddress())
                .country(product.getCountry())
                .zipCode(product.getZipCode())
                .phone(product.getPhone())
                .build();

        Product fullProduct = Product.builder()
                .productName(product.getProductName())
                .description(product.getDescription())
                .startPrice(parseFloat(product.getStartPrice()))
                .images(new ArrayList<>())
                .startDate(startDate)
                .user(user)
                .category(category)
                .subcategory(subcategory)
                .endDate(endDate)
                .info(shippingInfo)
                .build();

        fullProduct = productRepository.save(fullProduct);

        List<Image> images = new ArrayList<>();
        for (String imageUrl : product.getImages()) {
            try {
                byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);

                MultipartFile multipartFile = new ByteToMultipartFileConverter(imageBytes, imageUrl);

                String s3Url = s3Service.uploadFile(multipartFile, S3_KEY_PREFIX);

                Image image = new Image();
                image.setImageUrl(s3Url);
                image.setProduct(fullProduct);
                images.add(image);

            } catch (Exception e) {
                throw new IllegalStateException("Images not processable");
            }
            log.info("Finished processing CSV file");
        }

        fullProduct.setImages(images);
        productRepository.save(fullProduct);
    }

}
