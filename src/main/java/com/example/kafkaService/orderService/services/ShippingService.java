package com.example.kafkaService.orderService.services;

import com.example.kafkaService.orderService.exceptions.NotFoundOrderException;
import com.example.kafkaService.orderService.models.Order;
import com.example.kafkaService.orderService.repositories.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderRepository orderRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payed_orders")
    public void listenPayedOrders(String message) throws JsonProcessingException {
        Long orderId = Long.valueOf(message.substring(23));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundOrderException("Order not found"));
        LOGGER.info("Item with number {} packed and shipped", orderId);
        kafkaTemplate.send("sent_orders", objectMapper.writeValueAsString(order));
    }
}
