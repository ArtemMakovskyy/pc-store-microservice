package com.pc.stock.controller;

import com.pc.stock.dto.CreateMouseDto;
import com.pc.stock.dto.MouseDto;
import com.pc.stock.service.MouseService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mice")
@RequiredArgsConstructor
public class MouseController {
    private final MouseService mouseService;

    @PostMapping
    public ResponseEntity<MouseDto> create(@RequestBody CreateMouseDto createDto) {
        MouseDto mouse = mouseService.save(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mouse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MouseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mouseService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<MouseDto>> getAll() {
        return ResponseEntity.ok(mouseService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mouseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MouseDto> update(@PathVariable Long id, @RequestBody CreateMouseDto createDto) {
        return ResponseEntity.ok(mouseService.update(id, createDto));
    }

    @PatchMapping("/{id}/selling-price")
    public ResponseEntity<MouseDto> updateSellingPrice(@PathVariable Long id, @RequestParam BigDecimal newSellingPrice) {
        return ResponseEntity.ok(mouseService.updateSellingPrice(id, newSellingPrice));
    }
}
