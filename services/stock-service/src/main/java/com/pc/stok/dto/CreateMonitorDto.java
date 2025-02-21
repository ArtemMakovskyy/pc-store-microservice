package com.pc.stok.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateMonitorDto {

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

    @NotNull(message = "Resolution must not be null")
    @Size(min = 1, max = 50, message = "Resolution must be between 1 and 50 characters")
    private String resolution;
}
