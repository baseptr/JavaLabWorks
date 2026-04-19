package repository;

import entity.Beverage;

import java.util.List;

public interface BeverageRepository {
    List<Beverage> getAllBeverages();

    boolean addBeverage(Beverage beverage);

    Beverage deleteBeverage(long id);

    Beverage getBeverageById(long id);

    long getBeverageCount();
}
