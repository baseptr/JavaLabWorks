package com.esdc.lab2.service;

import com.esdc.lab2.entity.Coffee;
import com.esdc.lab2.entity.Order;
import com.esdc.lab2.repository.OrderRepository;

import java.util.List;

public class OrderService {
    public static final OrderService Instance = new OrderService();
    private final OrderRepository orderRepository = OrderRepository.Instance;

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public Order getOrderById(long id) {
        return orderRepository.getOrder(id);
    }

    public List<Order> getOrdersByCoffee(Coffee coffee) {
        return orderRepository.getOrdersByCoffeeId(coffee.getId());
    }

    public void createOrder(Order order) {
        orderRepository.save(order);
    }

    public long getOrdersCount(){
        return orderRepository.getCount();
    }

    public Double getTotalRevenue() {
        return orderRepository.getTotalRevenue();
    }


}
