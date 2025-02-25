package com.pc.stock.service.faignclient;

import com.pc.stock.dto.ComputerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ComputerFeignClient {
    @GetMapping("/api/computers/{id}")
    ComputerDto getById(@PathVariable("id") Long id);
}
