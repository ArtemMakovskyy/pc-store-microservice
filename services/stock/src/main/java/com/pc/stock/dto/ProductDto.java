package com.pc.stock.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

public interface ProductDto {
    String getPartNumber();
    String getName();
    String getDescription();
    BigDecimal getCostPrice();
    BigDecimal getSellingPrice();
}
