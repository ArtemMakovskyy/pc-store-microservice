package com.pc.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockItemDto {
    private Long id;
    private Integer quantity;
    private ProductDto product;
}