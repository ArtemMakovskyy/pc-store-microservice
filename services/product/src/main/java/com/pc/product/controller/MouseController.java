package com.pc.product.controller;

import com.pc.product.dto.CreateMouseDto;
import com.pc.product.dto.MouseDto;
import com.pc.product.service.impl.MouseService;
import jakarta.validation.Valid;
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
