package com.esdc.lab2.entity;

import lombok.Data;

@Data
public class Coffee {
    private Long id;
    private String name;
    private double price;

    public Coffee(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
    }
}
