package com.example.kafkaService.orderService.services;

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
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @KafkaListener(topics = "payed_orders")
    public void listenPayedOrders(String message){
        Long orderId = Long.valueOf(message.substring(23));
        LOGGER.info("Item with number {} packed and shipped", orderId);
        kafkaTemplate.send("sent_orders", "Shipped product with number:" + orderId);
    }
}
