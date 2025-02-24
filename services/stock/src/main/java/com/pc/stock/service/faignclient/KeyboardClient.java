package com.pc.stock.service.faignclient;

import com.pc.stock.dto.CreateKeyboardDto;
import com.pc.stock.dto.KeyboardDto;
import com.pc.stock.dto.MouseDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT-SERVICE")
public interface KeyboardClient {

    @GetMapping("/api/keyboards/{id}")
    KeyboardDto getById(
            @PathVariable("id") Long id);

    @GetMapping("/api/keyboards")
    List<KeyboardDto> getAll();

    @PostMapping("/api/keyboards")
    KeyboardDto create(
            @RequestBody CreateKeyboardDto createKeyboardDto);

    @PutMapping("/api/keyboards/{id}")
    KeyboardDto update(
            @PathVariable("id") Long id, @RequestBody CreateKeyboardDto createKeyboardDto);

    @DeleteMapping("/api/mice/{id}")
    void delete(@PathVariable("id") Long id);

    @PatchMapping("/api/keyboards/{id}/selling-price")
    KeyboardDto updateSellingPrice(
            @PathVariable("id") Long id, @RequestParam BigDecimal newSellingPrice);
}
