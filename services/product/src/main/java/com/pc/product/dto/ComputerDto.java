package com.pc.product.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ComputerDto {
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
    private String partNumber;
    private String name;
    private String description;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;

}
