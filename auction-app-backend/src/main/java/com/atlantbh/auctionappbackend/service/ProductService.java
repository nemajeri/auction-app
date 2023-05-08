package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.enums.SortBy;
import com.atlantbh.auctionappbackend.exception.CategoryNotFoundException;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.model.*;
import com.atlantbh.auctionappbackend.repository.*;
import com.atlantbh.auctionappbackend.request.NewProductRequest;
import com.atlantbh.auctionappbackend.response.ProductsResponse;
import com.atlantbh.auctionappbackend.response.SingleProductResponse;
import com.atlantbh.auctionappbackend.utils.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import static com.atlantbh.auctionappbackend.utils.Constants.S3_KEY_PREFIX;
import static com.atlantbh.auctionappbackend.utils.LevenshteinDistanceCalculation.calculate;


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


    public String getSuggestion(String query) {
        List<String> suggestions = productRepository.findTopNamesByNameSimilarity(query);

        if (suggestions.isEmpty()) {
            return null;
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

            return bestSuggestion;
        }
    }

    public List<Product> retrieveUserProductsByType(Long userId, SortBy sortingType) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Product> userProducts;

        if (sortingType == SortBy.SOLD) {
            userProducts = productRepository.findAllProductsByUserIdAndEndDateIsBefore(
                    userId, currentTime, Sort.by(Sort.Direction.DESC, SortBy.END_DATE.getSort()));
        } else {
            userProducts = productRepository.findAllProductsByUserIdAndEndDateIsAfter(
                    userId, currentTime, Sort.by(Sort.Direction.DESC, SortBy.START_DATE.getSort()));
        }

        return userProducts;
    }

    public List<ProductsResponse> getRecommendedProducts(Long userId) {
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<Product> recommendedProducts = productRepository.findRecommendedProducts(userId, pageRequest);
        return recommendedProducts.stream()
                .map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0), product.getCategory().getId()))
                .collect(Collectors.toList());
    }


    public Page<ProductsResponse> getAllFilteredProducts(int pageNumber, int pageSize, String searchTerm, Long categoryId) {
        Specification<Product> specification = Specification.where(ProductSpecifications.hasNameLike(searchTerm));

        if (categoryId != null) {
            specification = specification.and(ProductSpecifications.hasCategoryId(categoryId));
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> products = productRepository.findAll(specification, pageable);
        return products.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0), product.getCategory().getId()));
    }

    public void createProduct(NewProductRequest request, List<MultipartFile> images, HttpServletRequest httpServletRequest) {
        String token = tokenService.getJwtFromCookie(httpServletRequest);
        String email = tokenService.getClaimFromToken(token, "sub");

        Optional<AppUser> user = appUserRepository.getByEmail(email);
        AppUser appUser = user.get();
        Product product = Product.builder()
                .productName(request.getProductName())
                .description(request.getDescription())
                .startPrice(Float.parseFloat(request.getStartPrice()))
                .startDate(LocalDateTime.ofInstant(request.getStartDate().toInstant(), ZoneId.systemDefault()))
                .endDate(LocalDateTime.ofInstant(request.getEndDate().toInstant(), ZoneId.systemDefault()))
                .city(request.getCity())
                .zipCode(request.getZipCode())
                .country(request.getCountry())
                .phone(request.getPhone())
                .user(appUser)
                .build();

        Category category = categoryRepository.findById(Long.parseLong(request.getCategoryId())).orElseThrow(() -> new CategoryNotFoundException("Category not found."));
        product.setCategory(category);

        Subcategory subcategory = subcategoryRepository.findById(Long.parseLong(request.getCategoryId())).orElseThrow(() -> new CategoryNotFoundException("Category not found."));
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

    public Page<ProductsResponse> getNewProducts(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Product> products = productRepository.getNewArrivalsProducts(pageable);
        return products.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0), product.getCategory().getId()));
    }


    public SingleProductResponse getProductById(Long id, HttpServletRequest request) throws ProductNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        boolean isOwner = false;
        String jwt = tokenService.getJwtFromCookie(request);

        if (StringUtils.hasText(jwt) && tokenService.validateToken(jwt)) {
            String email = tokenService.getClaimFromToken(jwt, "sub");
            isOwner = product.isOwner(email);
        }

        SingleProductResponse response = new SingleProductResponse(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getStartPrice(),
                product.getImages(),
                product.getStartDate(),
                product.getEndDate(),
                product.getNumberOfBids(),
                product.getHighestBid(),
                isOwner
        );

        return response;
    }

    public Page<ProductsResponse> getLastProducts(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Product> products = productRepository.getLastChanceProducts(pageable);
        return products.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0), product.getCategory().getId()));
    }

    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products;
    }
}
