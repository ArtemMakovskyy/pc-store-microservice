package com.pc.stock.service;

import com.pc.stock.dto.CreateKeyboardDto;
import com.pc.stock.dto.KeyboardDto;
import com.pc.stock.service.faignclient.KeyboardFeignClient;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class KeyboardService {
    private final KeyboardFeignClient keyboardFeignClient;
    
    public KeyboardDto getById(Long id) {
        try {
            return CompletableFuture.supplyAsync(() -> keyboardFeignClient.getById(id))
                    .get();  
        } catch (Exception e) {
            log.error("Error fetching keyboard by ID", e);
            throw new RuntimeException("Error fetching keyboard by ID", e);
        }
    }
    
    public List<KeyboardDto> getAll() {
        try {
            return CompletableFuture.supplyAsync(() -> keyboardFeignClient.getAll())
                    .get();  
        } catch (Exception ex) {
            log.error("Error fetching all keyboards", ex);
            throw new RuntimeException("Error fetching all keyboards", ex);
        }
    }
    
    public KeyboardDto create(CreateKeyboardDto createKeyboardDto) {
        try {
            return CompletableFuture.supplyAsync(() -> keyboardFeignClient.create(createKeyboardDto))
                    .get();  
        } catch (Exception ex) {
            log.error("Error creating keyboard", ex);
            throw new RuntimeException("Error creating keyboard", ex);
        }
    }
    
    public KeyboardDto update(Long id, CreateKeyboardDto createKeyboardDto) {
        try {
            return CompletableFuture.supplyAsync(() -> keyboardFeignClient.update(id, createKeyboardDto))
                    .get();  
        } catch (Exception ex) {
            log.error("Error updating keyboard", ex);
            throw new RuntimeException("Error updating keyboard", ex);
        }
    }
    
    public void delete(Long id) {
        try {
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                keyboardFeignClient.delete(id);
                return null;
            });
            future.get();  
        } catch (Exception ex) {
            log.error("Error deleting keyboard", ex);
            throw new RuntimeException("Error deleting keyboard", ex);
        }
    }
    
    public KeyboardDto updateSellingPrice(Long id, BigDecimal newSellingPrice) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> keyboardFeignClient.updateSellingPrice(id, newSellingPrice))
                    .get();  
        } catch (Exception ex) {
            log.error("Error updating selling price", ex);
            throw new RuntimeException("Error updating selling price", ex);
        }
    }
}
