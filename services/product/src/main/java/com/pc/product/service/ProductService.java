package com.pc.product.service;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService<T, D> {
    D save(T product);
    D findById(Long id);
    List<D> findAll();
    boolean deleteById(Long id);
    D update(Long id, T product);
    D updateSellingPrice(Long id, BigDecimal newSellingPrice);
}
