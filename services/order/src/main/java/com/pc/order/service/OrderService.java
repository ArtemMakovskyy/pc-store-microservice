package com.pc.order.service;

import com.pc.order.dto.CustomerResponse;
import com.pc.order.dto.OrderConfirmation;
import com.pc.order.dto.OrderLineRequest;
import com.pc.order.dto.OrderRequest;
import com.pc.order.dto.OrderResponse;
import com.pc.order.dto.PaymentRequest;
import com.pc.order.dto.PurchaseRequest;
import com.pc.order.dto.PurchaseResponse;
import com.pc.order.dto.mapper.OrderMapper;
import com.pc.order.exception.BusinessException;
import com.pc.order.model.Order;
import com.pc.order.repository.OrderRepository;
import com.pc.order.service.fiignClient.CustomerClient;
import com.pc.order.service.fiignClient.PaymentClient;
import com.pc.order.service.kafka.OrderProducer;
import com.pc.order.service.resttemplateclient.ProductClient;
import jakarta.persistence.EntityNotFoundException;
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
    //REST Templates or Feign as we used before
    private final ProductClient productClient;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    @Transactional
    public Long createOrder(OrderRequest request) {
        final CustomerResponse customer
                = getCustomerIfExist(request.customerId());

        final List<PurchaseResponse> purchasedProduct
                = productClient.purchaseProductWithRestTemplate(request.products());

        Order order = repository.save(mapper.toOrder(request));

        final List<PurchaseRequest> productsList = request.products();
        persistOrderLines(productsList, order.getId());

        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);


        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProduct
                )
        );

        return order.getId();
    }

    private void persistOrderLines(List<PurchaseRequest> products, Long orderId) {
        for (PurchaseRequest purchaseRequest : products) {
            final OrderLineRequest orderLineRequest =
                    new OrderLineRequest(
                            null,
                            orderId,
                            purchaseRequest.productId(),
                            purchaseRequest.quantity());
            orderLineService.saveOrderLine(orderLineRequest);
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

    public OrderResponse findById(Integer id) {
        return this.repository.findById(id)
                .map(this.mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided ID: %d", id)));
    }


}
