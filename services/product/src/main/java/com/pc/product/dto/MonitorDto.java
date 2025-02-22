package com.pc.product.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class MonitorDto {
    private Long id;
    private String partNumber;
    private String name;
    private String description;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private String resolution;
}
