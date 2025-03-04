package com.pc.stock.service;

import com.pc.stock.dto.ProductDto;
import com.pc.stock.dto.StockItemDto;
import com.pc.stock.dto.mapper.StockItemMapper;
import com.pc.stock.model.StockItem;
import com.pc.stock.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockItemRepository stockItemRepository;
    private final StockItemMapper stockItemMapper;
    private final ProductService productService;

    public StockItemDto addStockItem(Long productId, String productType, Integer quantity) {
        ProductDto productDto = productService.getProductByType(productId, productType);

        StockItem stockItem = new StockItem();
        stockItem.setProductId(productId);
        stockItem.setProductType(productType);
        stockItem.setQuantity(quantity);
        stockItemRepository.save(stockItem);

        return new StockItemDto(stockItem.getId(), stockItem.getQuantity(), productDto);
    }

    public List<StockItemDto> getAllStockItems() {
        return stockItemRepository.findAll().stream()
                .map(stockItemMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public StockItemDto findBtId(Long id) {
        final StockItem stockItem = stockItemRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find stockItem by id: " + id));

        return stockItemMapper.mapToDto(stockItem);
    }
}
