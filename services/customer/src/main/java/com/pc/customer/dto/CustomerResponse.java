package com.pc.customer.dto;

public record CustomerResponse(
        String id,
        String name,
        String lastname,
        String email
) {

}
