package com.esdc.lab2.entity;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class Order {
    private Long id;
    private Long userId;
    private List<Coffee> items;
    private Instant createdAt;
    private double totalPrice;
}
