package com.pc.stock.dto.mapper;

import com.pc.stock.dto.ProductDto;
import com.pc.stock.dto.StockItemDto;
import com.pc.stock.model.StockItem;
import com.pc.stock.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockItemMapperImpl implements StockItemMapper {
    private final ProductService productService;

    @Override
    public StockItemDto mapToDto(StockItem stockItem) {
        ProductDto productDto = productService.getProductByType(
                stockItem.getProductId(), stockItem.getProductType()
        );
        return new StockItemDto(stockItem.getId(), stockItem.getQuantity(), productDto);
    }
}
