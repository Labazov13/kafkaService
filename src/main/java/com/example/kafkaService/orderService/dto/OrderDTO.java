package com.example.kafkaService.orderService.dto;

import java.math.BigDecimal;

public record OrderDTO (String name, BigDecimal price){}
