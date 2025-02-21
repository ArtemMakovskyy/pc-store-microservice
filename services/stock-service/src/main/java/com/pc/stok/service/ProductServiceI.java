package com.pc.stok.service;

import com.pc.stok.repository.ComputerRepository;
import com.pc.stok.repository.KeyboardRepository;
import com.pc.stok.repository.MonitorRepository;
import com.pc.stok.repository.MouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceI {
    private final MonitorRepository monitorRepository;
    private final ComputerRepository computerRepository;
    private final KeyboardRepository keyboardRepository;
    private final MouseRepository mouseRepository;

    public void save(){

    }
}
