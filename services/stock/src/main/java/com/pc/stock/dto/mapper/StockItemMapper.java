package com.pc.stock.dto.mapper;

import com.pc.stock.dto.StockItemDto;
import com.pc.stock.model.StockItem;

public interface StockItemMapper {
    StockItemDto mapToDto(StockItem stockItem);
}
