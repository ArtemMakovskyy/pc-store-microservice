package com.pc.stok.service;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService<T> {
    T save(T product);
    T findById(Long id);
    List<T> findAll();
    boolean deleteById(Long id);
    T update(Long id, T product);
    T updateSellingPrice(Long id, BigDecimal newSellingPrice);
}

