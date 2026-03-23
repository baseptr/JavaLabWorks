package com.esdc.lab2.service;

import com.esdc.lab2.entity.Coffee;
import com.esdc.lab2.entity.Order;
import com.esdc.lab2.entity.User;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class OrderProcessor {
    public static OrderProcessor Instance = new OrderProcessor();

    private final CoffeService coffeService;
    private final OrderService orderService;
    private final UserService userService;

    public OrderProcessor() {
        coffeService = CoffeService.Instance;
        orderService = OrderService.Instance;
        userService = UserService.Instance;
    }

    public void doOrder(String name, String[] idTokens) {
        String choice = String.join(" ", idTokens);
        createOrder(name, choice);
    }

    /** @deprecated Use {@link #doOrder(String, String[])} – kept for compatibility */
    public void doOrder() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Your Name");
        String name = scanner.nextLine();
        printMenu();
        System.out.println("Enter Your Choice (ids separated by spaces)");
        String choice = scanner.nextLine();

        createOrder(name, choice);
    }

    private void createOrder(String name, String choice) {
        User userByUserName = userService.getOrCreateUserByUserName(name);

        List<Long> beverageIds = parseIds(choice);

        List<Coffee> list = beverageIds.stream().map(coffeService::getCoffeeById).toList();

        Order order = new Order();
        order.setCreatedAt(Instant.now());
        order.setItems(list);
        order.setUserId(userByUserName.getId());

        list.stream().map(Coffee::getPrice).reduce(Double::sum).ifPresent(order::setTotalPrice);

        orderService.createOrder(order);
    }

    private List<Long> parseIds(String choice) {
        String[] s = choice.split(" ");
        return Arrays.stream(s).map(Long::parseLong).toList();
    }

    public void printMenu() {
        List<Coffee> allCoffees = CoffeService.Instance.getAllCoffees();
        System.out.println("Menu:");
        for (Coffee coffee : allCoffees) {
            System.out.println(coffee.getId() + ". " + coffee.getName() + " - $" + coffee.getPrice());
        }
    }
}
