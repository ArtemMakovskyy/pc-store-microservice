package com.pc.order.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String partNumber;
    private String name;
    private String description;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
}
