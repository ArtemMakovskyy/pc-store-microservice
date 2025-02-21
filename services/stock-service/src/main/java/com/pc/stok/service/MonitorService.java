package com.pc.stok.service;

import com.pc.stok.exception.ProductDataException;
import com.pc.stok.exception.ProductNotFoundException;
import com.pc.stok.model.Monitor;
import com.pc.stok.repository.MonitorRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MonitorService implements ProductService<Monitor> {
    private final MonitorRepository monitorRepository;

    @Override
    public Monitor save(Monitor product) {
        if (product.getPartNumber() == null || product.getName() == null || product.getSellingPrice() == null) {
            throw new ProductDataException("Part number, name, and selling price must not be null");
        }
        return monitorRepository.save(product);
    }

    @Override
    public Monitor findById(Long id) {
        return monitorRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Can't find Monitor by id: " + id));
    }

    @Override
    public List<Monitor> findAll() {
        return monitorRepository.findAll();
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if (!monitorRepository.existsById(id)) {
            throw new ProductNotFoundException("Monitor with id " + id + " does not exist");
        }
        monitorRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public Monitor update(Long id, Monitor updatedMonitor) {
        Monitor existingMonitor = findById(id);
        existingMonitor.setPartNumber(updatedMonitor.getPartNumber());
        existingMonitor.setName(updatedMonitor.getName());
        existingMonitor.setDescription(updatedMonitor.getDescription());
        existingMonitor.setCostPrice(updatedMonitor.getCostPrice());
        existingMonitor.setSellingPrice(updatedMonitor.getSellingPrice());
        existingMonitor.setResolution(updatedMonitor.getResolution());

        return monitorRepository.save(existingMonitor);
    }

    @Override
    @Transactional
    public Monitor updateSellingPrice(Long id, BigDecimal newSellingPrice) {
        if (newSellingPrice == null || newSellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductDataException("Selling price must be greater than zero");
        }
        Monitor monitor = findById(id);
        monitor.setSellingPrice(newSellingPrice);
        return monitorRepository.save(monitor);
    }
}
