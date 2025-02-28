package com.pc.customer.dto.mapper;

import com.pc.customer.dto.CustomerRequest;
import com.pc.customer.dto.CustomerResponse;
import com.pc.customer.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toCustomer(CustomerRequest request) {
        if (request == null) {
            return null;
        }
        return Customer.builder()
                .id(request.id())
                .name(request.name())
                .lastname(request.lastname())
                .email(request.email())
                .build();
    }

    public CustomerResponse fromCustomer(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getLastname(),
                customer.getEmail()
        );
    }
}
