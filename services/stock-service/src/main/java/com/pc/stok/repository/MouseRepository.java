package com.pc.stok.repository;

import com.pc.stok.model.Mouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MouseRepository extends JpaRepository<Mouse, Long> {
}
