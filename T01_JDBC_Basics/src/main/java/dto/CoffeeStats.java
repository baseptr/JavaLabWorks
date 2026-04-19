package dto;

import entity.Coffee;
import lombok.Data;


@Data
public class CoffeeStats {

    private Coffee coffee;
    private long totalOrders;
    private double revenue;
}
