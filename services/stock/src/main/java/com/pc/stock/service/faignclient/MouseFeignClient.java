package com.pc.stock.service.faignclient;

import com.pc.stock.dto.CreateMouseDto;
import com.pc.stock.dto.MouseDto;
import java.math.BigDecimal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "PRODUCT-SERVICE")
public interface MouseFeignClient {

    @GetMapping("/api/mice/{id}")
    MouseDto getById(@PathVariable("id") Long id);

    @GetMapping("/api/mice")
    List<MouseDto> getAll();

    @PostMapping("/api/mice")
    MouseDto create(@RequestBody CreateMouseDto createMouseDto);

    @PutMapping("/api/mice/{id}")
    MouseDto update(@PathVariable("id") Long id, @RequestBody CreateMouseDto createMouseDto);

    @DeleteMapping("/api/mice/{id}")
    void delete(@PathVariable("id") Long id);

    @PatchMapping("/api/mice/{id}/selling-price")
    MouseDto updateSellingPrice(@PathVariable("id") Long id, @RequestParam BigDecimal newSellingPrice);
}
