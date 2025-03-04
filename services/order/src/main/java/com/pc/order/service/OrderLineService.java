package com.pc.order.service;

import com.pc.order.dto.OrderLineRequest;
import com.pc.order.dto.OrderLineResponse;
import com.pc.order.dto.mapper.OrderLineMapper;
import com.pc.order.model.OrderLine;
import com.pc.order.repository.OrderLineRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLineService {

    private final OrderLineRepository repository;
    private final OrderLineMapper mapper;

    public Long saveOrderLine(OrderLine orderLine) {

        return repository.save(orderLine).getId();
    }

    public List<OrderLineResponse> findAllByOrderId(Long orderId) {
        return repository.findAllByOrderId(orderId)
                .stream()
                .map(mapper::toOrderLineResponse)
                .collect(Collectors.toList());
    }

}
