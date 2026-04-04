package com.esdc.lab3.service;

import com.esdc.lab3.entity.Beverage;
import com.esdc.lab3.repository.BeverageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@Lazy
public class DailyBeverageDiscountService {

    private static final Logger logger = LoggerFactory.getLogger(DailyBeverageDiscountService.class);
    private final BeverageRepository beverageRepository;
    private Beverage dailyDiscountBeverage;
    private double discountPercent;

    public DailyBeverageDiscountService(BeverageRepository beverageRepository) {
        this.beverageRepository = beverageRepository;
        logger.info("=== DailyBeverageDiscountService CREATED (constructor) ===");
    }

    @PostConstruct
    public void init() {
        logger.info("=== DailyBeverageDiscountService INITIALIZED (@PostConstruct) ===");
        updateDailyDiscount();
    }

    @PreDestroy
    public void destroy() {
        logger.info("=== DailyBeverageDiscountService DESTROYED (@PreDestroy) ===");
    }

    public void updateDailyDiscount() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        this.discountPercent = 10.0 + (dayOfWeek.getValue() - 1) * 2.5;

        List<Beverage> all = beverageRepository.getAllBeverages();
        if (!all.isEmpty()) {
            int index = today.getDayOfMonth() % all.size();
            this.dailyDiscountBeverage = all.get(index);
        }

        logger.info("Daily discount: {} — {}% off",
                dailyDiscountBeverage != null ? dailyDiscountBeverage.getName() : "N/A",
                discountPercent);
    }

    public Beverage getDailyDiscountBeverage() {
        return dailyDiscountBeverage;
    }

    public double applyDiscount(Beverage beverage) {
        return beverage.getPrice() * (1 - discountPercent / 100);
    }

    public double getDiscountPercent() {
        return discountPercent;
    }
}
