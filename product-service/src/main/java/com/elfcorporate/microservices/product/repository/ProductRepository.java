package com.elfcorporate.microservices.product.repository;

import com.elfcorporate.microservices.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
