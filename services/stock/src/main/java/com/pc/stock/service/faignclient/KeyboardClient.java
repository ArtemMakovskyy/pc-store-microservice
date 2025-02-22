package com.pc.stock.service.faignclient;

import com.pc.stock.dto.KeyboardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE")
public interface KeyboardClient {

    @GetMapping("/api/keyboards/{id}")
    KeyboardDto getById(@PathVariable("id") Long id);
}
