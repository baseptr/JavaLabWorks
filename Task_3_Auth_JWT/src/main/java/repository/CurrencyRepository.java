package repository;

import entity.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository{
 List<Currency> findAll();
 Optional<Double> findByType(String curType);
 void save(String curType, Double rate);
 void update(String curType, Double rate);
 void delete(String curType);
 boolean existByType(String curType);
}
