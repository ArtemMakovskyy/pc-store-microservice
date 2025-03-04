package com.pc.order.service.fiignClient;

import com.pc.order.dto.PaymentRequest;
import com.pc.order.dto.StockItemDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "STOCK-SERVICE")
public interface StockClient {

    @PostMapping("/api/stock")
    ResponseEntity<StockItemDto> addStockItem(
            @RequestParam Long productId,
            @RequestParam String productType,
            @RequestParam Integer quantity);

    @GetMapping("/api/stock")
    ResponseEntity<List<StockItemDto>> getAllStockItems();

    @GetMapping("/api/stock/{id}")
    ResponseEntity<StockItemDto> findStockItemsById(@PathVariable Long id);

}
