package com.pc.notificator.entity;

import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public void saveProduct() {
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(999.99);
        repository.save(product);
    }
}
