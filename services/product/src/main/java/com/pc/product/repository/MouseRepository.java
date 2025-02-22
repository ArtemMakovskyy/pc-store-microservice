package com.pc.product.repository;

import com.pc.product.model.Mouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MouseRepository extends JpaRepository<Mouse, Long> {
}
