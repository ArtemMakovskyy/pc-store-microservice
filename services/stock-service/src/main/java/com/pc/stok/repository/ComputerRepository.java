package com.pc.stok.repository;

import com.pc.stok.model.Computer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComputerRepository extends JpaRepository<Computer, Long> {
}
