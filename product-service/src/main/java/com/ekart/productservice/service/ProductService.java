package com.ekart.productservice.service;

import com.ekart.productservice.dto.ProductRequest;
import com.ekart.productservice.dto.ProductResponse;
import com.ekart.productservice.entity.Product;
import com.ekart.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public Boolean createProduct(ProductRequest productRequest){
        Product product=Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice()).build();
        Boolean isCreated=productRepository.save(product)!=null;
        log.info("Product {} is created", product.getId());
        return isCreated;
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products=productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product){
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
        return productResponse;
    }
}
