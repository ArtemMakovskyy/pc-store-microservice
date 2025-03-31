package com.pc.customer.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestUserControllerFotHtml {

    @PostMapping("/api")
    public String receiveFormData(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {

        // Логіка обробки даних
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        return "Data received: " + username + ", " + email;
    }
}
