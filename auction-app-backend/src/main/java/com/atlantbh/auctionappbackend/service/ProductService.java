package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.enums.SortBy;
import com.atlantbh.auctionappbackend.exception.ImageIndexNotFoundException;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.model.Category;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.repository.CategoryRepository;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
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


import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


import static com.atlantbh.auctionappbackend.utils.LevenshteinDistanceCalculation.calculate;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final TokenService tokenService;

    private final AppUserRepository appUserRepository;

    private final CategoryRepository categoryRepository;


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

    public Page<ProductsResponse> getAllFilteredProducts(int pageNumber, int pageSize, String searchTerm, Long categoryId) {
        Specification<Product> specification = Specification.where(ProductSpecifications.hasNameLike(searchTerm));

        if (categoryId != null) {
            specification = specification.and(ProductSpecifications.hasCategoryId(categoryId));
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> products = productRepository.findAll(specification, pageable);
        return products.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0), product.getCategory().getId()));
    }

    public Product createProduct(NewProductRequest request, HttpServletRequest httpServletRequest) {
        String token = tokenService.getJwtFromCookie(httpServletRequest);
        String email = tokenService.getClaimFromToken(token, "sub");

        Optional<AppUser> user = appUserRepository.getByEmail(email);
        AppUser appUser = user.get();
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setStartPrice(Float.parseFloat(request.getStartPrice()));
        product.setStartDate(LocalDateTime.ofInstant(request.getStartDate().toInstant(), ZoneId.systemDefault()));
        product.setEndDate(LocalDateTime.ofInstant(request.getEndDate().toInstant(), ZoneId.systemDefault()));
        product.setUser(appUser);

        Category category = categoryRepository.findById(Long.parseLong(request.getCategoryId())).orElseThrow(() -> new NoSuchElementException("Category not found."));
        product.setCategory(category);

        return productRepository.save(product);
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

    public void addProductImage(Long productId, String imageUrl) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        product.getImages().add(imageUrl);
        productRepository.save(product);
    }
}
