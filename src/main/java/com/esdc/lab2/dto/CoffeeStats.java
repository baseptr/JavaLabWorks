package com.esdc.lab2.dto;

import com.esdc.lab2.entity.Coffee;
import lombok.Data;


@Data
public class CoffeeStats {

    private Coffee coffee;
    private long totalOrders;
    private double revenue;
}
