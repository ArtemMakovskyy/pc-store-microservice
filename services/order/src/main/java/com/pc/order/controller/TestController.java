package com.pc.order.controller;

import com.pc.order.dto.CustomerResponse;
import com.pc.order.service.fiignClient.CustomerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
  private final   CustomerClient customerClient;
    @GetMapping()
    public ResponseEntity<CustomerResponse> findById() {
        return ResponseEntity.ok(this.customerClient.findCustomerById("67c1e3f0c16b9e2fa37de512").get());
    }
}
