package com.pc.stock.service;

import com.pc.stock.dto.ProductDto;
import com.pc.stock.service.faignclient.ComputerFeignClient;
import com.pc.stock.service.faignclient.KeyboardFeignClient;
import com.pc.stock.service.faignclient.MonitorFeignClient;
import com.pc.stock.service.faignclient.MouseFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ComputerFeignClient computerFeignClient;
    private final MonitorFeignClient monitorFeignClient;
    private final KeyboardService keyboardService;
    private final KeyboardFeignClient keyboardFeignClient;
    private final MouseFeignClient mouseFeignClient;
    private final MouseService mouseService;

    public ProductDto getProductByType(Long productId, String productType) {
        return switch (productType.toLowerCase()) {
            case "computer" -> computerFeignClient.getById(productId);
            case "monitor" -> monitorFeignClient.getById(productId);
            case "keyboard" -> keyboardService.getById(productId);
            case "mouse" -> mouseService.getById(productId);
            default -> throw new IllegalArgumentException("Unknown product type: " + productType);
        };
    }
}
