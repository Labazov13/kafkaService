package com.example.kafkaService.orderService.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderService orderService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "new_orders")
    public void listenNewOrders(String message){
        Long orderId = Long.valueOf(message.substring(18));
        orderService.updateOrder(orderId);
        kafkaTemplate.send("payed_orders", "Paid order with number:" + orderId);
    }
}
