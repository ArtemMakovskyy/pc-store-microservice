package com.pc.stok.service;

import com.pc.stok.dto.ComputerDto;
import com.pc.stok.model.Computer;
import java.math.BigDecimal;
import java.util.List;

public interface PcConfigService {
    List<ComputerDto> restockConfigurations(List<String> partNumbers, BigDecimal percentageIncrease);
}
