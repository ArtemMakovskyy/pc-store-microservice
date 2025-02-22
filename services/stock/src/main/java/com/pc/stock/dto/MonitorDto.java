package com.pc.stock.dto;

import java.math.BigDecimal;
import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonitorDto implements ProductDto {
    private String partNumber;
    private String name;
    private String description;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
}
