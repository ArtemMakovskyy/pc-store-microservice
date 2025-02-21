package com.pc.stok.model;

import java.math.BigDecimal;

public interface Product {
    String getPartNumber();
    String getName();
    String getDescription();
    BigDecimal getCostPrice();
    BigDecimal getSellingPrice();

}
