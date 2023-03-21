package com.atlantbh.auctionappbackend.mapper;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setProductName(product.getProductName());
        productDTO.setDescription(product.getDescription());
        productDTO.setStartPrice(product.getStartPrice());
        productDTO.setImages(product.getImages());
        productDTO.setStartDate(product.getStartDate());
        productDTO.setEndDate(product.getEndDate());
        return productDTO;
    }

    public Product toProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setStartPrice(productDTO.getStartPrice());
        product.setImages(productDTO.getImages());
        product.setStartDate(productDTO.getStartDate());
        product.setEndDate(productDTO.getEndDate());
        return product;
    }
}
