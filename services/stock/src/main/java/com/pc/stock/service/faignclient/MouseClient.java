package com.pc.stock.service.faignclient;

import com.pc.stock.dto.MouseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "PRODUCT-SERVICE")
public interface MouseClient {
    @GetMapping("/api/mice/{id}")
    MouseDto getById(@PathVariable("id") Long id);
}
