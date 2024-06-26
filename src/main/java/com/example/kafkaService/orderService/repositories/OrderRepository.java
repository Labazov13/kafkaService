package com.example.kafkaService.orderService.repositories;

import com.example.kafkaService.orderService.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
