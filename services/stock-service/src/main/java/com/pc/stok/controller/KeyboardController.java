package com.pc.stok.controller;

import com.pc.stok.dto.CreateKeyboardDto;
import com.pc.stok.dto.KeyboardDto;
import com.pc.stok.service.impl.KeyboardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/keyboards")
@RequiredArgsConstructor
public class KeyboardController {

    private final KeyboardService keyboardService;

    @PostMapping
    public ResponseEntity<KeyboardDto> saveKeyboard(
            @RequestBody @Valid CreateKeyboardDto createKeyboardDto) {
        KeyboardDto keyboardDto = keyboardService.save(createKeyboardDto);
        return new ResponseEntity<>(keyboardDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KeyboardDto> getKeyboardById(@PathVariable Long id) {
        KeyboardDto keyboardDto = keyboardService.findById(id);
        return new ResponseEntity<>(keyboardDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<KeyboardDto>> getAllKeyboards() {
        List<KeyboardDto> keyboardDtos = keyboardService.findAll();
        return new ResponseEntity<>(keyboardDtos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKeyboard(@PathVariable Long id) {
        if (keyboardService.deleteById(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<KeyboardDto> updateKeyboard(
            @PathVariable Long id, @RequestBody @Valid CreateKeyboardDto createKeyboardDto) {
        KeyboardDto keyboardDto = keyboardService.update(id, createKeyboardDto);
        return new ResponseEntity<>(keyboardDto, HttpStatus.OK);
    }

    @PatchMapping("/{id}/selling-price")
    public ResponseEntity<KeyboardDto> updateKeyboardSellingPrice(
            @PathVariable Long id, @RequestParam BigDecimal newSellingPrice) {
        KeyboardDto keyboardDto = keyboardService.updateSellingPrice(id, newSellingPrice);
        return new ResponseEntity<>(keyboardDto, HttpStatus.OK);
    }
}
