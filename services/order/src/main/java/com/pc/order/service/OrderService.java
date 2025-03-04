package com.pc.order.service;

import com.pc.order.dto.CustomerResponse;
import com.pc.order.dto.OrderConfirmation;
import com.pc.order.dto.OrderRequest;
import com.pc.order.dto.OrderResponse;
import com.pc.order.dto.PaymentRequest;
import com.pc.order.dto.PurchaseRequest;
import com.pc.order.dto.PurchaseResponse;
import com.pc.order.dto.StockItemDto;
import com.pc.order.dto.mapper.OrderMapper;
import com.pc.order.exception.BusinessException;
import com.pc.order.model.Order;
import com.pc.order.model.OrderLine;
import com.pc.order.repository.OrderRepository;
import com.pc.order.service.fiignClient.CustomerClient;
import com.pc.order.service.fiignClient.PaymentClient;
import com.pc.order.service.fiignClient.StockClient;
import com.pc.order.service.kafka.KafkaOrderProducer;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final CustomerClient customerClient;
    private final PaymentClient paymentClient;
    private final StockClient stockClient;
    private final OrderLineService orderLineService;
    private final KafkaOrderProducer kafkaOrderProducer;

    @Transactional
    public Long createOrder(OrderRequest request) {
        final CustomerResponse customer
                = getCustomerIfExist(request.customerId());

        List<PurchaseResponse> purchaseResponses = new ArrayList<>();
        for (PurchaseRequest product : request.products()) {
            StockItemDto stockItemDto
                    = stockClient.findStockItemsById(
                    product.productId()).getBody();

            PurchaseResponse purchaseResponse = new PurchaseResponse(
                    product.productId(),
                    stockItemDto.getProduct().getName(),
                    stockItemDto.getProduct().getDescription(),
                    stockItemDto.getProduct().getCostPrice(),
                    stockItemDto.getQuantity());
            purchaseResponses.add(purchaseResponse);
        }

        final Order savedOrder = repository.save(mapper.toOrder(request));

        final List<PurchaseRequest> productsList = request.products();
        System.out.println(productsList.size());
        productsList.forEach(System.out::println);
        persistOrderLines(productsList, savedOrder);

        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                savedOrder.getId(),
                savedOrder.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        kafkaOrderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchaseResponses
                )
        );

        return savedOrder.getId();
    }

    private void persistOrderLines(List<PurchaseRequest> products, Order order) {

        for (PurchaseRequest purchaseRequest : products) {

            OrderLine orderLine = new OrderLine();
            orderLine.setProductId(purchaseRequest.productId());
            orderLine.setOrder(order);
            orderLine.setQuantity(purchaseRequest.quantity());
            orderLineService.saveOrderLine(orderLine);
        }
    }

    private CustomerResponse getCustomerIfExist(String customerId) {
        return this.customerClient.findCustomerById(customerId)
                .orElseThrow(() -> new BusinessException(
                        "Cannot create order:: No customer exists with the provided ID"));

    }

    public List<OrderResponse> findAllOrders() {
        return this.repository.findAll()
                .stream()
                .map(this.mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Long id) {
        return this.repository.findById(id)
                .map(this.mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided ID: %d", id)));
    }

}
