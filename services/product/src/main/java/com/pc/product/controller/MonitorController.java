package com.pc.product.controller;

import com.pc.product.dto.CreateMonitorDto;
import com.pc.product.dto.MonitorDto;
import com.pc.product.service.impl.MonitorService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/monitors")
@RequiredArgsConstructor
public class MonitorController {

    private final MonitorService monitorService;

    @PostMapping
    public ResponseEntity<MonitorDto> saveMonitor(
            @RequestBody @Valid CreateMonitorDto createMonitorDto) {
        MonitorDto monitorDto = monitorService.save(createMonitorDto);
        return new ResponseEntity<>(monitorDto, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<MonitorDto> getMonitorById(@PathVariable Long id) {
        MonitorDto monitorDto = monitorService.findById(id);
        return new ResponseEntity<>(monitorDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MonitorDto>> getAllMonitors() {
        List<MonitorDto> monitorDtos = monitorService.findAll();
        return new ResponseEntity<>(monitorDtos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonitor(@PathVariable Long id) {
        if (monitorService.deleteById(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonitorDto> updateMonitor(
            @PathVariable Long id, @RequestBody @Valid CreateMonitorDto createMonitorDto) {
        MonitorDto monitorDto = monitorService.update(id, createMonitorDto);
        return new ResponseEntity<>(monitorDto, HttpStatus.OK);
    }

    @PatchMapping("/{id}/selling-price")
    public ResponseEntity<MonitorDto> updateMonitorSellingPrice(
            @PathVariable Long id, @RequestParam BigDecimal newSellingPrice) {
        MonitorDto monitorDto = monitorService.updateSellingPrice(id, newSellingPrice);
        return new ResponseEntity<>(monitorDto, HttpStatus.OK);
    }
}
