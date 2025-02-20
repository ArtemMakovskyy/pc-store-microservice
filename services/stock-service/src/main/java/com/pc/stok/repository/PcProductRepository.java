package com.pc.stok.repository;

import com.pc.stok.model.PcProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PcProductRepository extends JpaRepository<PcProduct, Long> {
}
