package com.pc.order.service.fiignClient;

import com.pc.order.dto.CustomerResponse;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerClient {
    @GetMapping("/api/customers/{id}")
    Optional<CustomerResponse> findCustomerById(@PathVariable("id") String customerId);
}
