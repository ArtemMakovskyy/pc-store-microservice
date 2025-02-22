package com.pc.product.service;

import com.pc.product.dto.ComputerDto;
import java.math.BigDecimal;
import java.util.List;

public interface PcConfigService {
    List<ComputerDto> addFromParser(List<String> partNumbers, BigDecimal percentageIncrease);
}
