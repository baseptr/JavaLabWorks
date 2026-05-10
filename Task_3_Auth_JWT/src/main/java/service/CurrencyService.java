package service;

import entity.Currency;
import exception.CurrencyAlreadyExistsException;
import exception.CurrencyStorageEmptyException;
import exception.CurrencyStorageNoSuchTypeException;
import exception.RandomDeleteException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import repository.CurrencyRepository;

import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final Random rand = new Random();

    public List<Currency> getAll(Integer limit) {
        List<Currency> all = currencyRepository.findAll();
        if (all.isEmpty()) {
            log.warn("No currencies in storage");
            throw new CurrencyStorageEmptyException("No currencies found");
        }
        return (limit == 0) ? all : all.stream().limit(limit).toList();
    }

    public Double getByType(String curType) {
        return currencyRepository.findByType(curType)
                .orElseThrow(() -> {
                    log.warn("No such currency: {}", curType);
                    return new CurrencyStorageNoSuchTypeException("No such type of currency: " + curType);
                });
    }

    public void create(String curType, Double rate) {
        if (currencyRepository.existByType(curType)) {
            throw new CurrencyAlreadyExistsException("Currency already exists: " + curType);
        }
        currencyRepository.save(curType, rate);
        log.info("New currency rate successfully added: {}", curType);
    }

    public void update(String curType, Double rate) {
        if (!currencyRepository.existByType(curType)) {
            throw new CurrencyStorageNoSuchTypeException("No such type of currency: " + curType);
        }
        currencyRepository.update(curType, rate);
        log.info("The rate has successfully updated for {}", curType);
    }

    public void delete(String curType) {
        if (!currencyRepository.existByType(curType)) {
            log.warn("Attempt to delete non-existent currency: {}", curType);
            throw new CurrencyStorageNoSuchTypeException("No such type of currency: " + curType);
        }
        if (rand.nextBoolean()) {
            throw new RandomDeleteException("Rand delete ex");
        } else {
            currencyRepository.delete(curType);
            log.info("Currency deleted: {}", curType);
        }
    }
}
