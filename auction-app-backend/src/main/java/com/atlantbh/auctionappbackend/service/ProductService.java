package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.mapper.ProductMapper;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service()
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public List<ProductDTO> getNewProducts(int pageNumber, int size) {
        int offset = pageNumber * size;
        List<Product> products = productRepository.getNewArrivalsProducts(size, offset);
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<ProductDTO>>() {
        }.getType();
        List<ProductDTO> productDTOs = modelMapper.map(products, listType);

        return productDTOs;
    }

    public List<ProductDTO> getLastProducts(int pageNumber, int size) {
        int offset = pageNumber * size;
        List<Product> products = productRepository.getLastChanceProducts(size, offset);

        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<ProductDTO>>() {
        }.getType();
        List<ProductDTO> productDTOs = modelMapper.map(products, listType);

        return productDTOs;
    }

    public ProductDTO getProductById(Long id) throws ProductNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (!optionalProduct.isPresent()) {
            throw new ProductNotFoundException(id);
        }

        Product product = optionalProduct.get();
        ProductDTO productDTO = productMapper.toProductDTO(product);

        return productDTO;
    }
}
