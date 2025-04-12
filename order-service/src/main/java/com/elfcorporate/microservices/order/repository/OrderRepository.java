package com.elfcorporate.microservices.order.repository;

import com.elfcorporate.microservices.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
