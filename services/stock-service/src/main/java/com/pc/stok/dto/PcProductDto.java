package com.pc.stok.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PcProductDto {
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
}
