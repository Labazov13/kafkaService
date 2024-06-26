package com.example.kafkaService.orderService.services;

import com.example.kafkaService.orderService.dto.OrderDTO;
import com.example.kafkaService.orderService.exceptions.NotFoundOrderException;
import com.example.kafkaService.orderService.models.Order;
import com.example.kafkaService.orderService.repositories.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.LockModeType;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public Order createOrder(OrderDTO orderDTO) throws JsonProcessingException {
        Order newOrder = new Order(orderDTO.name(), orderDTO.price(), "CREATED", orderDTO.email());
        orderRepository.save(newOrder);
        LOGGER.info("An order with number {} was created and saved to the database", newOrder.getId());
        sendMessage(newOrder);
        return newOrder;
    }

    public void sendMessage(Order order) throws JsonProcessingException {
        kafkaTemplate.send("new_orders", objectMapper.writeValueAsString(order));
    }
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void updateOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundOrderException("Order not found"));
        order.setStatus("PAID");
        orderRepository.save(order);
    }
}
