package com.pc.stock.service;

import com.pc.stock.dto.ProductDto;
import com.pc.stock.service.faignclient.ComputerClient;
import com.pc.stock.service.faignclient.KeyboardClient;
import com.pc.stock.service.faignclient.MonitorClient;
import com.pc.stock.service.faignclient.MouseClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ComputerClient computerClient;
    private final MonitorClient monitorClient;
    private final KeyboardClient keyboardClient;
    private final MouseClient mouseClient;

    public ProductDto getProductByType(Long productId, String productType) {
        return switch (productType.toLowerCase()) {
            case "computer" -> computerClient.getById(productId);
            case "monitor" -> monitorClient.getById(productId);
            case "keyboard" -> keyboardClient.getById(productId);
            case "mouse" -> mouseClient.getById(productId);
            default -> throw new IllegalArgumentException("Unknown product type: " + productType);
        };
    }
}
