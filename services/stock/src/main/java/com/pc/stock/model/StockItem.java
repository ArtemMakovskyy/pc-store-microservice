package com.pc.stock.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "stock_items")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE stock_items SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class StockItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productType;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private boolean isDeleted = false;
}
