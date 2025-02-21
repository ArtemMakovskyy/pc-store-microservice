package com.pc.stok.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateComputerDto {

    @Size(max = 100, message = "CPU URL must be at most 100 characters")
    private String cpu;

    @Size(max = 255, message = "CPU URL must be at most 255 characters")
    private String cpuUrl;

    @Size(max = 100, message = "Motherboard URL must be at most 100 characters")
    private String motherboard;

    @Size(max = 255, message = "Motherboard URL must be at most 255 characters")
    private String motherboardUrl;

    @Size(max = 100, message = "Memory URL must be at most 100 characters")
    private String memory;

    @Size(max = 255, message = "Memory URL must be at most 255 characters")
    private String memoryUrl;

    @Size(max = 100, message = "GPU URL must be at most 100 characters")
    private String gpu;

    @Size(max = 255, message = "GPU URL must be at most 255 characters")
    private String gpuUrl;

    @Size(max = 100, message = "SSD URL must be at most 100 characters")
    private String ssd;

    @Size(max = 255, message = "SSD URL must be at most 255 characters")
    private String ssdUrl;

    @Size(max = 100, message = "Power supplier URL must be at most 100 characters")
    private String powerSupplier;

    @Size(max = 255, message = "Power supplier URL must be at most 255 characters")
    private String powerSupplierUrl;

    @Positive(message = "Prediction FPS must be greater than zero")
    private Integer predictionFps;

    @Positive(message = "Gaming score must be greater than zero")
    private Double gamingScore;

    @Positive(message = "Price for FPS must be greater than zero")
    private Integer priceForFps;

    @NotNull(message = "Part number must not be null")
    @Size(min = 1, max = 50, message = "Part number must be between 1 and 50 characters")
    private String partNumber;

    @NotNull(message = "Name must not be null")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;

    @NotNull(message = "Cost price must not be null")
    @Positive(message = "Cost price must be greater than zero")
    private BigDecimal costPrice;

    @NotNull(message = "Selling price must not be null")
    @Positive(message = "Selling price must be greater than zero")
    private BigDecimal sellingPrice;
}
