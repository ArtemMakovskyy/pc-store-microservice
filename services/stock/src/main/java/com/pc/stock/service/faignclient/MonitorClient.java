package com.pc.stock.service.faignclient;

import com.pc.stock.dto.MonitorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE")
public interface MonitorClient {

    @GetMapping("/api/monitors/{id}")
    MonitorDto getById(@PathVariable("id") Long id);
}
