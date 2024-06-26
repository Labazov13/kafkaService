package com.example.kafkaService.orderService.services;

import com.example.kafkaService.orderService.dto.OrderDTO;
import com.example.kafkaService.orderService.exceptions.NotFoundOrderException;
import com.example.kafkaService.orderService.models.Order;
import com.example.kafkaService.orderService.repositories.OrderRepository;
import jakarta.persistence.LockModeType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OrderService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private final OrderRepository orderRepository;

    public Order createOrder(OrderDTO orderDTO) {
        Order newOrder = new Order(orderDTO.name(), orderDTO.price(), "CREATED");
        orderRepository.save(newOrder);
        sendMessage(newOrder);
        return newOrder;
    }

    public void sendMessage(Order order) {
        kafkaTemplate.send("new_orders", "Order with number:" + order.getId());
    }
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void updateOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundOrderException("Order not found"));
        order.setStatus("PAID");
        orderRepository.save(order);
        kafkaTemplate.send("new_orders", "Order with number: " + orderId + "was paid");
    }
}
