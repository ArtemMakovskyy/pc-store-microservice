package com.pc.stok.model;

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
@Table(name = "computers")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE computers SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class Computer extends AbstractProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    private Integer predictionFps;
    private Double gamingScore;
    private Integer priceForFps;
    @Column(nullable = false)
    private boolean isDeleted = false;

}
