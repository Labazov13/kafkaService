package com.example.kafkaService.orderService.services;

import com.example.kafkaService.orderService.models.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationsService {
    private final ObjectMapper objectMapper;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    public NotificationsService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "sent_orders")
    public void listenSentOrders(String message) {
        try {
            Order order = objectMapper.readValue(message, Order.class);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(order.getEmail());
            mailMessage.setSubject("The order has been sent!");
            mailMessage.setText("The order has been sent! Order details: " + objectMapper.writeValueAsString(order));
            javaMailSender.send(mailMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
