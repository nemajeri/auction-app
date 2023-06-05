package com.atlantbh.auctionappbackend.utils;

import com.atlantbh.auctionappbackend.model.Product;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import static com.atlantbh.auctionappbackend.utils.Constants.SEARCH_VALIDATOR;

@UtilityClass
public class ProductSpecifications {
    public static Specification<Product> hasNameLike(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.isEmpty() || !searchTerm.trim().matches(SEARCH_VALIDATOR)) {
                return criteriaBuilder.disjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + searchTerm.toLowerCase() + "%");
        };
    }

    public static Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }
}
