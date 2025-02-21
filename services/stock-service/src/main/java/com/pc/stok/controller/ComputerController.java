package com.pc.stok.controller;

import com.pc.stok.dto.AddComputersFromParserRequestDto;
import com.pc.stok.dto.CreateComputerDto;
import com.pc.stok.dto.ComputerDto;
import com.pc.stok.service.impl.ComputerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/computers")
@RequiredArgsConstructor
public class ComputerController {

    private final ComputerService computerService;

    @PostMapping
    public ResponseEntity<ComputerDto> createComputer(
            @RequestBody @Valid CreateComputerDto createComputerDto) {
        ComputerDto computerDto = computerService.save(createComputerDto);
        return new ResponseEntity<>(computerDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ComputerDto>> getAllComputers() {
        List<ComputerDto> computers = computerService.findAll();
        return new ResponseEntity<>(computers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComputerDto> getComputerById(@PathVariable Long id) {
        ComputerDto computerDto = computerService.findById(id);
        return new ResponseEntity<>(computerDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComputerDto> updateComputer(
            @PathVariable Long id, @RequestBody @Valid CreateComputerDto createComputerDto) {
        ComputerDto updatedComputer = computerService.update(id, createComputerDto);
        return new ResponseEntity<>(updatedComputer, HttpStatus.OK);
    }

    @PutMapping("/{id}/selling-price")
    public ResponseEntity<ComputerDto> updateSellingPrice(
            @PathVariable Long id, @RequestParam BigDecimal newSellingPrice) {
        ComputerDto updatedComputer = computerService.updateSellingPrice(id, newSellingPrice);
        return new ResponseEntity<>(updatedComputer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComputer(@PathVariable Long id) {
        computerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/restock")
    public ResponseEntity<List<ComputerDto>> restockComputers(
            @RequestBody AddComputersFromParserRequestDto requestDto) {
        List<ComputerDto> updatedComputers = computerService
                .addFromParser(requestDto.getPartNumbers(), requestDto.getPercentageIncrease());
        return new ResponseEntity<>(updatedComputers, HttpStatus.OK);
    }

}
