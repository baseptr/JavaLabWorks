package com.esdc.lab2.repository;

import com.esdc.lab2.dto.CoffeeStats;
import com.esdc.lab2.entity.Coffee;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CoffeeRepository {
    public static final CoffeeRepository Instance = new CoffeeRepository();
    private DataSource dataSource = DataSourceProvider.Instance.getDataSource();

    public List<Coffee> getAllCoffees() {
        String sql = "select * from coffee";
        List<Coffee> res = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Coffee coffee = new Coffee(rs.getString("name"), rs.getDouble("price"), 0);
                coffee.setId(rs.getLong("id"));
                res.add(coffee);
            }

        } catch (SQLException ex) {
            log.error("Failed execute getAllCoffees\n{}", ex.getSQLState());
        }
        return res;
    }

    public Coffee getCoffeById(long id) {
        String sql = "select * from coffee where id = ?";
        Coffee res = new Coffee("",0.,1);
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    res.setPrice(rs.getDouble("price"));
                    res.setId(rs.getLong("id"));
                    res.setName(rs.getString("name"));
                }
            }
        } catch (SQLException ex) {
            log.error("Failed execute getCoffeeById\n{}", ex.getSQLState());
        }
        return res;
    }

    public CoffeeStats getMosPopularCoffee() {
        String sql = """
                select c.id, c.name, c.price, count(oi.order_id) total_orders
                from coffee c
                join order_items oi on c.id = oi.coffee_id
                group by c.id, c.name, c.price
                order by total_orders desc
                limit 1
                """;
        Coffee cof = new Coffee("",0.,1);
        CoffeeStats res = new CoffeeStats();
        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if(rs.next()){
                cof.setName(rs.getString("name"));
                cof.setId(rs.getLong("id"));
                cof.setPrice(rs.getDouble("price"));
                res.setCoffee(cof);
                res.setTotalOrders(rs.getLong("total_orders"));
                res.setRevenue(cof.getPrice() * res.getTotalOrders());
            }

        } catch (SQLException ex) {
            log.error("Failed execute getMosPopularCoffee\n{}", ex.getSQLState());
        }
        return res;
    }

    public CoffeeStats getLeastPopularCoffee() {
        String sql = """
                select c.id, c.name, c.price, count(oi.order_id) total_orders
                from coffee c
                join order_items oi on c.id = oi.coffee_id
                group by c.id, c.name, c.price
                order by total_orders asc
                limit 1
                """;
        Coffee cof = new Coffee("",0.,1);
        CoffeeStats res = new CoffeeStats();
        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if(rs.next()){
                cof.setName(rs.getString("name"));
                cof.setId(rs.getLong("id"));
                cof.setPrice(rs.getDouble("price"));
                res.setCoffee(cof);
                res.setTotalOrders(rs.getLong("total_orders"));
                res.setRevenue(cof.getPrice() * res.getTotalOrders());
            }

        } catch (SQLException ex) {
            log.error("Failed execute getLeastPopularCoffee\n{}", ex.getSQLState());
        }
        return res;
    }

    public CoffeeStats getMostRevenueGeneratingCoffee() {
        String sql = """
                select c.id, c.name, c.price, sum(c.price) revenue
                from coffee c
                join order_items oi on c.id = oi.coffee_id
                group by c.id, c.name, c.price
                order by revenue desc
                limit 1
                """;
        Coffee cof = new Coffee("",0.,1);
        CoffeeStats res = new CoffeeStats();
        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if(rs.next()){
                cof.setName(rs.getString("name"));
                cof.setId(rs.getLong("id"));
                cof.setPrice(rs.getDouble("price"));
                res.setCoffee(cof);
                res.setRevenue(rs.getDouble("revenue"));
                res.setTotalOrders((long) (res.getRevenue() / cof.getPrice()));
            }

        } catch (SQLException ex) {
            log.error("Failed execute getMostRevenueGeneratingCoffee\n{}", ex.getSQLState());
        }
        return res;
    }
}
