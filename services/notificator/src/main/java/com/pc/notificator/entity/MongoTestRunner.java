package com.pc.notificator.entity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MongoTestRunner implements CommandLineRunner {
    private final ProductService productService;

    public MongoTestRunner(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) {
        productService.saveProduct();
        System.out.println("Product saved to MongoDB!");
    }
}
