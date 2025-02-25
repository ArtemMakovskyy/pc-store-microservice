package com.pc.stock.controller;

import com.pc.stock.dto.CreateMonitorDto;
import com.pc.stock.dto.MonitorDto;
import com.pc.stock.service.webclient.MonitorWebClient;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/stock/monitors")
@RequiredArgsConstructor
@Slf4j
public class MonitorController {
    private final MonitorWebClient monitorWebClient;

    @PostMapping
    public Mono<ResponseEntity<MonitorDto>> saveMonitor(@RequestBody CreateMonitorDto createMonitorDto) {
        return monitorWebClient.saveMonitor(createMonitorDto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<MonitorDto>> getMonitorById(@PathVariable Long id) {
        return monitorWebClient.getMonitorById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Mono<ResponseEntity<List<MonitorDto>>> getAllMonitors() {
        return monitorWebClient.getAllMonitors()
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteMonitor(@PathVariable Long id) {
        return monitorWebClient.deleteMonitor(id)
                .map(response -> ResponseEntity.noContent().build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<MonitorDto>> updateMonitor(
            @PathVariable Long id,
            @RequestBody CreateMonitorDto createMonitorDto) {
        return monitorWebClient.updateMonitor(id, createMonitorDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/selling-price")
    public Mono<ResponseEntity<MonitorDto>> updateMonitorSellingPrice(
            @PathVariable Long id,
            @RequestParam BigDecimal newSellingPrice) {
        return monitorWebClient.updateMonitorSellingPrice(id, newSellingPrice)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
