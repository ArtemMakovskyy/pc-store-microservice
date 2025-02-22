package com.pc.product.service.impl;

import com.pc.product.dto.CreateMonitorDto;
import com.pc.product.dto.MonitorDto;
import com.pc.product.dto.mapper.MonitorMapper;
import com.pc.product.exception.ProductDataException;
import com.pc.product.exception.ProductNotFoundException;
import com.pc.product.model.Monitor;
import com.pc.product.repository.MonitorRepository;
import com.pc.product.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MonitorService implements ProductService<CreateMonitorDto, MonitorDto> {
    private final MonitorRepository monitorRepository;
    private final MonitorMapper monitorMapper;

    @Override
    public MonitorDto save(CreateMonitorDto createDto) {
        if (createDto.getPartNumber() == null || createDto.getName() == null || createDto.getSellingPrice() == null) {
            throw new ProductDataException("Part number, name, and selling price must not be null");
        }
        final Monitor monitor = monitorMapper.toEntity(createDto);
        return monitorMapper.toDto(monitorRepository.save(monitor));
    }

    @Override
    public MonitorDto findById(Long id) {
        return monitorRepository.findById(id)
                .map(monitorMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException("Can't find Monitor by id: " + id));
    }

    @Override
    public List<MonitorDto> findAll() {
        List<Monitor> monitors = monitorRepository.findAll();
        return monitors.stream()
                .map(monitorMapper::toDto)
                .toList();
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
    public MonitorDto update(Long id, CreateMonitorDto createMonitorDto) {
        Monitor existingMonitor = monitorRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Can't find Monitor by id: " + id));

        final Monitor monitor = monitorMapper.toEntity(createMonitorDto);
        existingMonitor.setPartNumber(monitor.getPartNumber());
        existingMonitor.setName(monitor.getName());
        existingMonitor.setDescription(monitor.getDescription());
        existingMonitor.setCostPrice(monitor.getCostPrice());
        existingMonitor.setSellingPrice(monitor.getSellingPrice());
        existingMonitor.setResolution(monitor.getResolution());

        return monitorMapper.toDto(monitorRepository.save(existingMonitor));
    }

    @Override
    @Transactional
    public MonitorDto updateSellingPrice(Long id, BigDecimal newSellingPrice) {
        if (newSellingPrice == null || newSellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductDataException("Selling price must be greater than zero");
        }
        Monitor monitor = monitorRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Can't find Monitor by id: " + id));

        monitor.setSellingPrice(newSellingPrice);
        return monitorMapper.toDto(monitorRepository.save(monitor));
    }
}
