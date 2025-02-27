package com.pc.customer.service;


import com.pc.customer.dto.CustomerRequest;
import com.pc.customer.dto.CustomerResponse;
import com.pc.customer.dto.mapper.CustomerMapper;
import com.pc.customer.exception.CustomerNotFoundException;
import com.pc.customer.model.Customer;
import com.pc.customer.repository.CustomerRepository;
import com.pc.customer.validator.CustomerValidator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerValidator<CustomerRequest> validator;
    private final CustomerMapper mapper;

    public String createCustomer(CustomerRequest request) {
        this.validator.validate(request);
        var customer = this.repository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest request) {
        this.validator.validate(request);
        var customer = this.repository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Cannot update customer:: No customer found with the provided ID: %s", request.id())
                ));
        mergeCustomer(customer, request);
        this.repository.save(customer);
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if (StringUtils.isNotBlank(request.name())) {
            customer.setName(request.name());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        customer.setAge(request.age());
    }

    public List<CustomerResponse> findAllCustomers() {
        return  this.repository.findAll()
                .stream()
                .map(this.mapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public CustomerResponse findById(String id) {
        return this.repository.findById(id)
                .map(mapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("No customer found with the provided ID: %s", id)));
    }

    public boolean existsById(String id) {
        return this.repository.findById(id)
                .isPresent();
    }

    public void deleteCustomer(String id) {
        this.repository.deleteById(id);
    }
}
