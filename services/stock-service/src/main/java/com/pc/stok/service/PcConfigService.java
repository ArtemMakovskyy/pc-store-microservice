package com.pc.stok.service;

import com.pc.stok.dto.ComputerDto;
import java.math.BigDecimal;
import java.util.List;

public interface PcConfigService {
    List<ComputerDto> addFromParser(List<String> partNumbers, BigDecimal percentageIncrease);
}
