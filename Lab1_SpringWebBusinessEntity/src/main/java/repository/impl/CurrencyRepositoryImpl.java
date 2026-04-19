package repository.impl;

import entity.Currency;
import org.springframework.stereotype.Repository;
import repository.CurrencyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class CurrencyRepositoryImpl implements CurrencyRepository {
    private List<Currency> currencies = new ArrayList<>();

    @Override
    public List<Currency> findAll() {
        return currencies;
    }

    @Override
    public Optional<Double> findByType(String curType) {
       return currencies.stream()
               .filter(c -> c.getType().equals(curType))
               .map(Currency::getRate)
               .findFirst();
    }

    @Override
    public void save(String curType, Double rate) {
        currencies.add(new Currency(curType,rate));
    }

    @Override
    public void update(String curType, Double rate) {
       currencies.stream()
                .filter(c -> c.getType().equals(curType))
                .findFirst()
                .ifPresent(c -> c.setRate(rate));
    }

    @Override
    public void delete(String curType) {
       currencies = currencies.stream()
               .filter(c -> !c.getType().equals(curType))
               .toList();

    }

    @Override
    public boolean existByType(String curType) {
        return currencies.stream().anyMatch(c -> c.getType().equals(curType));
    }
}
