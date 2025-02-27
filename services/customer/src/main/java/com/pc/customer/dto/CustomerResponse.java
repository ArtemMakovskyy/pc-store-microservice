package com.pc.customer.dto;

public record CustomerResponse(
        String id,
        String name,
        String email,
        int age
) {

}
