package com.pc.payment.controller;

import com.pc.payment.dto.PaymentRequest;
import com.pc.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @GetMapping
    public String r(){
        return "r";
    }

//    @PostMapping
//    public ResponseEntity<Long> createPayment(
//            @RequestBody @Valid PaymentRequest request) {
//        return ResponseEntity.ok(this.service.createPayment(request));
//    }

    @PostMapping
    public ResponseEntity<Long> createPayment(
            @RequestBody @Valid PaymentRequest request) {
        System.out.println(request);
        return ResponseEntity.ok(this.service.createPayment(request));
    }
}
