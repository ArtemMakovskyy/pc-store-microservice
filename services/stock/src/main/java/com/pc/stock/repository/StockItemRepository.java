package com.pc.stock.repository;

import com.pc.stock.model.StockItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    List<StockItem> findByProductId(Long productId);
}
