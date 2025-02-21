package com.pc.stok.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class MouseDto  {
    private Long id;
    private String partNumber;
    private String name;
    private String description;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
}
