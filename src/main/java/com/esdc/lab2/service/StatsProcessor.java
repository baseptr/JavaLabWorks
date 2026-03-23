package com.esdc.lab2.service;

import com.esdc.lab2.dto.CoffeeStats;
import com.esdc.lab2.entity.User;

import java.util.List;
import java.util.Map;

public class StatsProcessor {

    abstract static class StatCalculator implements Runnable {

        abstract String getName();

    }

    public static StatsProcessor Instance = new StatsProcessor();


    private final Map<Integer, StatCalculator> stats = Map.of(
            1, new StatCalculator() {
                @Override
                String getName() {
                    return "Most popular coffee";
                }

                @Override
                public void run() {
                    CoffeeStats mostPopularCoffee = CoffeService.Instance.getMostPopularCoffee();
                    System.out.println("Most popular coffee: " + mostPopularCoffee.getCoffee() + " with " + mostPopularCoffee + " orders");
                }
            },
            2, new StatCalculator() {
                @Override
                String getName() {
                    return "Least popular coffee";
                }

                @Override
                public void run() {
                    System.out.println("Least popular coffee: " + CoffeService.Instance.getLeastPopularCoffee());
                }
            },
            3, new StatCalculator() {
                @Override
                String getName() {
                    return "Most revenue generating coffee";
                }

                @Override
                public void run() {
                    System.out.println("Most revenue generating coffee: " + CoffeService.Instance.getMostRevenueGeneratingCoffee());
                }
            },
            4, new StatCalculator() {
                @Override
                String getName() {
                    return "Users who ordered most popular coffee";
                }

                @Override
                public void run() {
                    CoffeeStats mostPopularCoffee = CoffeService.Instance.getMostPopularCoffee();
                    List<User> usersByMostOrderedCoffee = UserService.Instance.getUsersByMostOrderedCoffee(mostPopularCoffee.getCoffee());
                    System.out.println("Users who ordered most popular coffee: " + usersByMostOrderedCoffee);
                }
            },
            5, new StatCalculator() {
                @Override
                String getName() {
                    return "Users who ordered least popular coffee";
                }

                @Override
                public void run() {
                    CoffeeStats leastPopularCoffee = CoffeService.Instance.getLeastPopularCoffee();
                    List<User> usersByMostOrderedCoffee = UserService.Instance.getUsersByMostOrderedCoffee(leastPopularCoffee.getCoffee());
                    System.out.println("Users who ordered least popular coffee: " + usersByMostOrderedCoffee);
                }
            },
            6, new StatCalculator() {
                @Override
                String getName() {
                    return "Total orders";
                }

                @Override
                public void run() {
                    System.out.println("Total orders: " + OrderService.Instance.getOrdersCount());
                }
            },
            7, new StatCalculator() {
                @Override
                String getName() {
                    return "Total revenue";
                }

                @Override
                public void run() {
                    System.out.println("Total revenue: " + OrderService.Instance.getTotalRevenue());
                }
            },
            8, new StatCalculator() {
                @Override
                String getName() {
                    return "User Who Ordered the most Least Popular Coffee";
                }

                @Override
                public void run() {
                    CoffeeStats leastPopularCoffee = CoffeService.Instance.getLeastPopularCoffee();
                    System.out.println("User : " + UserService.Instance.getUserWhoOrderedMost(leastPopularCoffee.getCoffee()));
                }
            });


public void printStatsMenu() {
    System.out.println("Stats Menu:");
    stats.forEach((k, v) -> System.out.println(k + ". " + v.getName()));
}

public void showStats(int statId) {
    StatCalculator statCalculator = stats.get(statId);
    if (statCalculator != null) {
        statCalculator.run();
    } else {
        System.out.println("Invalid choice");
    }
}

}
