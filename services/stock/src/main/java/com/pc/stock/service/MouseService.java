package com.pc.stock.service;

import com.pc.stock.dto.CreateMouseDto;
import com.pc.stock.dto.MouseDto;
import com.pc.stock.service.faignclient.MouseClient;
import feign.FeignException;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MouseService {

    private final MouseClient mouseClient;

    public MouseDto save(CreateMouseDto createDto) {
        try {
            return mouseClient.create(createDto);
        } catch (FeignException ex) {
            log.error("Error occurred while creating mouse: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to create mouse. Please try again later.");
        }
    }

    public MouseDto getById(Long id) {
        try {
            return mouseClient.getById(id);
        } catch (FeignException ex) {
            log.error("Error occurred while fetching mouse with ID {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Mouse not found with ID " + id);
        }
    }

    public List<MouseDto> findAll() {
        try {
            return mouseClient.getAll();
        } catch (FeignException ex) {
            log.error("Error occurred while fetching all mice: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to fetch mice list. Please try again later.");
        }
    }

    @Async
    public void deleteById(Long id) {
        try {
            mouseClient.delete(id);
        } catch (FeignException ex) {
            log.error("Error occurred while deleting mouse with ID {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Failed to delete mouse with ID " + id);
        }
    }

    public MouseDto update(Long id, CreateMouseDto createMouseDto) {
        try {
            return mouseClient.update(id, createMouseDto);
        } catch (FeignException ex) {
            log.error("Error occurred while updating mouse with ID {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Failed to update mouse with ID " + id);
        }
    }


    public MouseDto updateSellingPrice(Long id, BigDecimal newSellingPrice) {
        try {
            return mouseClient.updateSellingPrice(id, newSellingPrice);
        } catch (FeignException ex) {
            log.error("Error occurred while updating selling price for mouse with ID {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Failed to update selling price for mouse with ID " + id);
        }
    }
}
