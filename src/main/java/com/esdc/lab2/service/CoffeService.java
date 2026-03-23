package com.esdc.lab2.service;

import com.esdc.lab2.dto.CoffeeStats;
import com.esdc.lab2.entity.Coffee;
import com.esdc.lab2.repository.CoffeeRepository;

import java.util.List;

public class CoffeService {
    private final CoffeeRepository coffeeRepository = CoffeeRepository.Instance;

    public static CoffeService Instance = new CoffeService();

    List<Coffee> getAllCoffees() {
        return coffeeRepository.getAllCoffees();
     }

     Coffee getCoffeeById(long id) {
        return coffeeRepository.getCoffeById(id);
     }

     CoffeeStats getMostPopularCoffee() {
        return coffeeRepository.getMosPopularCoffee();
     }

    CoffeeStats getLeastPopularCoffee() {
        return coffeeRepository.getLeastPopularCoffee();
     }

    CoffeeStats getMostRevenueGeneratingCoffee() {
        return coffeeRepository.getMostRevenueGeneratingCoffee();
     }


}
