package com.pc.stok.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class AddComputersFromParserRequestDto {
    private List<String> partNumbers;
    private BigDecimal percentageIncrease;
}
