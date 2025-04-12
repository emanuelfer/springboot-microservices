package com.elfcorporate.microservices.product.service;

import com.elfcorporate.microservices.product.dto.ProductRequest;
import com.elfcorporate.microservices.product.dto.ProductResponse;
import com.elfcorporate.microservices.product.model.Product;
import com.elfcorporate.microservices.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .skuCode(productRequest.skuCode())
                .price(productRequest.price())
                .build();
        productRepository.save(product);
        log.info("Product created successfully");

        return new ProductResponse(product.getId(), product.getName(), product.getSkuCode(), product.getDescription(), product.getPrice());
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getSkuCode(), product.getDescription(), product.getPrice()))
                .toList();
    }
}
