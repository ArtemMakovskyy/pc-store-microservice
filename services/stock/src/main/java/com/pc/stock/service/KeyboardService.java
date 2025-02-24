package com.pc.stock.service;

import com.pc.stock.dto.CreateKeyboardDto;
import com.pc.stock.dto.KeyboardDto;
import com.pc.stock.service.faignclient.KeyboardClient;
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
    private final KeyboardClient keyboardClient;
    
    public KeyboardDto getById(Long id) {
        try {
            return CompletableFuture.supplyAsync(() -> keyboardClient.getById(id))
                    .get();  
        } catch (Exception e) {
            log.error("Error fetching keyboard by ID", e);
            throw new RuntimeException("Error fetching keyboard by ID", e);
        }
    }
    
    public List<KeyboardDto> getAll() {
        try {
            return CompletableFuture.supplyAsync(() -> keyboardClient.getAll())
                    .get();  
        } catch (Exception ex) {
            log.error("Error fetching all keyboards", ex);
            throw new RuntimeException("Error fetching all keyboards", ex);
        }
    }
    
    public KeyboardDto create(CreateKeyboardDto createKeyboardDto) {
        try {
            return CompletableFuture.supplyAsync(() -> keyboardClient.create(createKeyboardDto))
                    .get();  
        } catch (Exception ex) {
            log.error("Error creating keyboard", ex);
            throw new RuntimeException("Error creating keyboard", ex);
        }
    }
    
    public KeyboardDto update(Long id, CreateKeyboardDto createKeyboardDto) {
        try {
            return CompletableFuture.supplyAsync(() -> keyboardClient.update(id, createKeyboardDto))
                    .get();  
        } catch (Exception ex) {
            log.error("Error updating keyboard", ex);
            throw new RuntimeException("Error updating keyboard", ex);
        }
    }
    
    public void delete(Long id) {
        try {
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                keyboardClient.delete(id);
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
                    () -> keyboardClient.updateSellingPrice(id, newSellingPrice))
                    .get();  
        } catch (Exception ex) {
            log.error("Error updating selling price", ex);
            throw new RuntimeException("Error updating selling price", ex);
        }
    }
}
