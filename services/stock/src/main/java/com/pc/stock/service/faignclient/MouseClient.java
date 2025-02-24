package com.pc.stock.service.faignclient;

import com.pc.stock.dto.MouseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "PRODUCT-SERVICE")
public interface MouseClient {

    @GetMapping("/api/mice/{id}")
    MouseDto getById(@PathVariable("id") Long id);

    @GetMapping("/api/mice")
    List<MouseDto> getAll();

    @PostMapping("/api/mice")
    MouseDto create(@RequestBody MouseDto mouseDto);

    @PutMapping("/api/mice/{id}")
    MouseDto update(@PathVariable("id") Long id, @RequestBody MouseDto mouseDto);

    @DeleteMapping("/api/mice/{id}")
    void delete(@PathVariable("id") Long id);
}
