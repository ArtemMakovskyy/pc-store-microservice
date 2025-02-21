package com.pc.stok.controller;

import com.pc.stok.dto.CreateMouseDto;
import com.pc.stok.dto.MouseDto;
import com.pc.stok.service.impl.MouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/mice")
@RequiredArgsConstructor
public class MouseController {

    private final MouseService mouseService;

    @PostMapping
    public ResponseEntity<MouseDto> saveMouse(
            @RequestBody @Valid CreateMouseDto createMouseDto) {
        MouseDto mouseDto = mouseService.save(createMouseDto);
        return new ResponseEntity<>(mouseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MouseDto> getMouseById(@PathVariable Long id) {
        MouseDto mouseDto = mouseService.findById(id);
        return new ResponseEntity<>(mouseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MouseDto>> getAllMice() {
        List<MouseDto> mouseDtos = mouseService.findAll();
        return new ResponseEntity<>(mouseDtos, HttpStatus.OK);
    }

    //todo fixed it
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMouse(@PathVariable Long id) {
        if (mouseService.deleteById(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MouseDto> updateMouse(
            @PathVariable Long id, @RequestBody @Valid CreateMouseDto createMouseDto) {
        MouseDto mouseDto = mouseService.update(id, createMouseDto);
        return new ResponseEntity<>(mouseDto, HttpStatus.OK);
    }

    //todo fixed it
    @PatchMapping("/{id}/selling-price")
    public ResponseEntity<MouseDto> updateMouseSellingPrice(
            @PathVariable Long id, @RequestParam BigDecimal newSellingPrice) {
        MouseDto mouseDto = mouseService.updateSellingPrice(id, newSellingPrice);
        return new ResponseEntity<>(mouseDto, HttpStatus.OK);
    }
}
