package com.pc.order.dto;

public record CustomerResponse(
    String id,
    String name,
    String lastname,
    String email
) {

}
