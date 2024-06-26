package com.example.kafkaService.orderService.services;

import com.example.kafkaService.orderService.models.Order;
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
public class PaymentService {

    private final OrderService orderService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(topics = "new_orders")
    public void listenNewOrders(String message) {
        try {
            Order order = objectMapper.readValue(message, Order.class);
            if (order.getStatus().equals("CREATED")) {
                orderService.updateOrder(order.getId());
                kafkaTemplate.send("payed_orders", "Paid order with number:" + order.getId());
                LOGGER.info("The order has been paid and transferred for shipment");
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Something went wrong...");
        }
    }
}
