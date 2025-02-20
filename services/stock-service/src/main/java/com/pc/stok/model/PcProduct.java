package com.pc.stok.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "pc_products")
@Getter
@Setter
@SQLDelete(sql = "UPDATE pc_products SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class PcProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long partNumber;
    private String cpu;
    private String cpuUrl;
    private String motherboard;
    private String motherboardUrl;
    private String memory;
    private String memoryUrl;
    private String gpu;
    private String gpuUrl;
    private String ssd;
    private String ssdUrl;
    private String powerSupplier;
    private String powerSupplierUrl;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private Integer predictionFps;
    private Double gamingScore;
    private Integer priceForFps;
    private Boolean IsBestPrice;
    @Column(nullable = false)
    private boolean isDeleted = false;
}
