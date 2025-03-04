package com.pc.stock.controller;

import com.pc.stock.dto.StockItemDto;
import com.pc.stock.dto.mapper.StockItemMapper;
import com.pc.stock.service.StockService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @PostMapping
    public ResponseEntity<StockItemDto> addStockItem(
            @RequestParam Long productId,
            @RequestParam String productType,
            @RequestParam Integer quantity) {
        StockItemDto stockItem = stockService.addStockItem(productId, productType, quantity);
        return new ResponseEntity<>(stockItem, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StockItemDto>> getAllStockItems() {
        List<StockItemDto> stockItems = stockService.getAllStockItems();
        return new ResponseEntity<>(stockItems, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockItemDto> findStockItemsById(@PathVariable Long id) {
        return new ResponseEntity<>(stockService.findBtId(id), HttpStatus.OK);
    }

}
