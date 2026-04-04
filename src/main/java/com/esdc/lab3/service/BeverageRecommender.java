package com.esdc.lab3.service;

import com.esdc.lab3.entity.Beverage;
import com.esdc.lab3.repository.BeverageRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BeverageRecommender {

    private final BeverageRepository beverageRepository;
    private final Map<Long, Integer> orderHistory = new HashMap<>();

    public BeverageRecommender(BeverageRepository beverageRepository) {
        this.beverageRepository = beverageRepository;
    }

    public void recordSelection(List<Beverage> beverages) {
        for (Beverage beverage : beverages) {
            orderHistory.merge(beverage.getId(), 1, Integer::sum);
        }
    }

    public Beverage recommendBasedOnHistory() {
        if (orderHistory.isEmpty()) {
            return recommendRandom();
        }

        Long recommendedId = orderHistory.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        return recommendedId != null ? beverageRepository.getBeverageById(recommendedId) : recommendRandom();
    }

    public Beverage recommendNew() {
        List<Beverage> all = beverageRepository.getAllBeverages();
        List<Beverage> notOrdered = all.stream()
                .filter(b -> !orderHistory.containsKey(b.getId()))
                .toList();

        if (notOrdered.isEmpty()) {
            return recommendRandom();
        }

        return notOrdered.get(ThreadLocalRandom.current().nextInt(notOrdered.size()));
    }

    public Beverage recommendRandom() {
        List<Beverage> all = beverageRepository.getAllBeverages();
        if (all.isEmpty()) {
            return null;
        }
        return all.get(ThreadLocalRandom.current().nextInt(all.size()));
    }

    public List<Map.Entry<Long, Integer>> getTopOrdered(int limit) {
        return orderHistory.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(limit)
                .toList();
    }
}
