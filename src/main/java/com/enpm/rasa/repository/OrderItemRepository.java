package com.enpm.rasa.repository;

import com.enpm.rasa.model.Order;
import com.enpm.rasa.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findByOrderId(Order order);
}
