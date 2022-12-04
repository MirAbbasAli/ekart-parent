package com.ekart.productservice.repository;

import com.ekart.productservice.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {

}
