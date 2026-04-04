package com.esdc.lab3.service;


import com.esdc.lab3.entity.Beverage;
import com.esdc.lab3.repository.BeverageRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BeverageSelectorRandom implements BeverageSelector {
    private final int amountOfBeverages = ThreadLocalRandom.current().nextInt(1, 5);
    //private final CoffeeService beverageService;
    private final BeverageRepository beverageRepository;

    public BeverageSelectorRandom(BeverageRepository beverageRepository) {
        this.beverageRepository = beverageRepository;
    }

    @Override
    public List<Beverage> selectBeverage() {
        long beverageCount = beverageRepository.getBeverageCount();
        List<Beverage> order = new ArrayList<>();
        for (int i = 0; i < amountOfBeverages; i++) {
            long randomId = ThreadLocalRandom.current().nextLong(1, beverageCount + 1);
            Beverage beverageById = beverageRepository.getBeverageById(randomId);
            order.add(beverageById);
        }
        return order;
    }
}
