package com.pc.order.repository;

import com.pc.order.model.OrderLine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

    List<OrderLine> findAllByOrderId(Long orderId);
}
