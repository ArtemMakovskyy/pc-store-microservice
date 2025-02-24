package com.pc.stock.controller;

import com.pc.stock.dto.CreateKeyboardDto;
import com.pc.stock.dto.KeyboardDto;
import com.pc.stock.service.KeyboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/keyboards")
@RequiredArgsConstructor
@Log4j2
public class KeyboardController {
    private final KeyboardService keyboardService;

    @GetMapping("/{id}")
    public ResponseEntity<KeyboardDto> getById(@PathVariable Long id) {
        KeyboardDto result = keyboardService.getById(id);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            log.error("Keyboard with ID " + id + " not found.");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<KeyboardDto>> getAll() {
        List<KeyboardDto> result = keyboardService.getAll();
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            log.error("Error fetching all keyboards.");
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<KeyboardDto> create(@RequestBody CreateKeyboardDto createKeyboardDto) {
        KeyboardDto result = keyboardService.create(createKeyboardDto);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            log.error("Error creating keyboard.");
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<KeyboardDto> update(@PathVariable Long id, @RequestBody CreateKeyboardDto createKeyboardDto) {
        KeyboardDto result = keyboardService.update(id, createKeyboardDto);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            log.error("Error updating keyboard with ID " + id + ".");
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            keyboardService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            log.error("Error deleting keyboard with ID " + id + ": ", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/{id}/selling-price")
    public ResponseEntity<KeyboardDto> updateSellingPrice(@PathVariable Long id, @RequestParam BigDecimal newSellingPrice) {
        KeyboardDto result = keyboardService.updateSellingPrice(id, newSellingPrice);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            log.error("Error updating selling price for keyboard with ID " + id + ".");
            return ResponseEntity.badRequest().build();
        }
    }
}
