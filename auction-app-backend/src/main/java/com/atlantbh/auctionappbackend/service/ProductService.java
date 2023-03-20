package com.atlantbh.auctionappbackend.service.serviceImpl;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.mapper.ProductMapper;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import com.atlantbh.auctionappbackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service()
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    public List<ProductDTO> getProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty())
            return new ArrayList<>() {
            };

        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<ProductDTO>>() {
        }.getType();
        List<ProductDTO> productDTOs = modelMapper.map(products, listType);

        return productDTOs;
    }

    @Override
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
