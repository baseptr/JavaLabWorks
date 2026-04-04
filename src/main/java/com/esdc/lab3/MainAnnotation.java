package com.esdc.lab3;

import com.esdc.lab3.entity.Beverage;
import com.esdc.lab3.service.BeverageRecommender;
import com.esdc.lab3.service.BeverageSelector;
import com.esdc.lab3.service.CoffeeService;
import com.esdc.lab3.service.DailyBeverageDiscountService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@ComponentScan(basePackages = "com.esdc.lab3")
public class MainAnnotation {

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MainAnnotation.class);
        CoffeeService scenarioService = ctx.getBean(CoffeeService.class);

        scenarioService.getAllBeverages();


        System.out.println("\n=== Prototype Scope ===");
        BeverageSelector selector1 = ctx.getBean(BeverageSelector.class);
        BeverageSelector selector2 = ctx.getBean(BeverageSelector.class);
        BeverageSelector selector3 = ctx.getBean(BeverageSelector.class);

        List<Beverage> beverages1 = selector1.selectBeverage();
        List<Beverage> beverages2 = selector2.selectBeverage();
        List<Beverage> beverages3 = selector3.selectBeverage();

        System.out.println("Selector 1: " + selector1 + " — выбрал " + beverages1.size() + " напитков");
        System.out.println("Selector 2: " + selector2 + " — выбрал " + beverages2.size() + " напитков");
        System.out.println("Selector 3: " + selector3 + " — выбрал " + beverages3.size() + " напитков");

        List<Beverage> beverages = selector1.selectBeverage();

        BeverageRecommender recommender = ctx.getBean(BeverageRecommender.class);
        recommender.recordSelection(beverages);

        System.out.println("\n=== Recommendations ===");
        System.out.println("Popular pick: " + recommender.recommendBasedOnHistory().getName());
        System.out.println("Try something new: " + recommender.recommendNew().getName());
        System.out.println("Random choice: " + recommender.recommendRandom().getName());

        System.out.println("\n=== Waiting 3 seconds for Lazy bean... ===");
        Thread.sleep(3000);

        System.out.println("Requesting DailyBeverageDiscountService (should be initialized now)...");
        DailyBeverageDiscountService discountService = ctx.getBean(DailyBeverageDiscountService.class);

        Beverage dailyBeverage = discountService.getDailyDiscountBeverage();
        double discountedPrice = discountService.applyDiscount(dailyBeverage);

        System.out.println("Beverage of the day: " + dailyBeverage.getName());
        System.out.println("Original price: $" + dailyBeverage.getPrice());
        System.out.printf("Discounted price (%.1f%% off): $%.2f%n",
                discountService.getDiscountPercent(), discountedPrice);

        ctx.close();
    }
}
