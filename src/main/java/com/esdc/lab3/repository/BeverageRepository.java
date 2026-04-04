package com.esdc.lab3.repository;

import com.esdc.lab3.entity.Beverage;

import java.util.List;

public interface BeverageRepository {
    List<Beverage> getAllBeverages();

    boolean addBeverage(Beverage beverage);

    Beverage deleteBeverage(long id);

    Beverage getBeverageById(long id);

    long getBeverageCount();
}
