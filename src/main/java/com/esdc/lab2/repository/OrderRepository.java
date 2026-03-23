package com.esdc.lab2.repository;

import com.esdc.lab2.entity.Coffee;
import com.esdc.lab2.entity.Order;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderRepository {
    public static final OrderRepository Instance = new OrderRepository();
    private final DataSource dataSource = DataSourceProvider.Instance.getDataSource();

    public long getCount() {
        String sql = "select count(*) from orders";
        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException ex) {
            log.error("Failed execute getCount\n{}", ex.getSQLState());
        }
        return 0;
    }

    public List<Order> getAllOrders() {
        String sql = "select id, user_id, total_price, created_at from orders";
        List<Order> res = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getLong("id"));
                order.setUserId(rs.getLong("user_id"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                order.setItems(getItemsByOrderId(con, order.getId()));
                res.add(order);
            }
        } catch (SQLException ex) {
            log.error("Failed execute getAllOrders\n{}", ex.getSQLState());
        }
        return res;
    }

    public Order getOrder(long id) {
        String sql = "select id, user_id, total_price, created_at from orders where id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getLong("id"));
                    order.setUserId(rs.getLong("user_id"));
                    order.setTotalPrice(rs.getDouble("total_price"));
                    order.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                    order.setItems(getItemsByOrderId(con, order.getId()));
                    return order;
                }
            }
        } catch (SQLException ex) {
            log.error("Failed execute getOrder\n{}", ex.getSQLState());
        }
        return null;
    }

    public List<Order> getOrdersByCoffeeId(Long coffeeId) {
        String sql = """
                select distinct o.id, o.user_id, o.total_price, o.created_at
                from orders o
                join order_items oi on o.id = oi.order_id
                where oi.coffee_id = ?
                """;
        List<Order> res = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, coffeeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getLong("id"));
                    order.setUserId(rs.getLong("user_id"));
                    order.setTotalPrice(rs.getDouble("total_price"));
                    order.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                    order.setItems(getItemsByOrderId(con, order.getId()));
                    res.add(order);
                }
            }
        } catch (SQLException ex) {
            log.error("Failed execute getOrdersByCoffeeId\n{}", ex.getSQLState());
        }
        return res;
    }

    public Order save(Order order) {
        String insertOrder = "insert into orders(user_id, total_price, created_at) values (?, ?, ?) returning id";
        String insertItem = "insert into order_items(order_id, coffee_id) values (?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement psOrder = con.prepareStatement(insertOrder);
             PreparedStatement psItem = con.prepareStatement(insertItem)) {

            psOrder.setLong(1, order.getUserId());
            psOrder.setDouble(2, order.getTotalPrice());
            psOrder.setTimestamp(3, Timestamp.from(order.getCreatedAt()));
            try (ResultSet rs = psOrder.executeQuery()) {
                if (rs.next()) {
                    order.setId(rs.getLong("id"));
                }
            }

            for (Coffee coffee : order.getItems()) {
                psItem.setLong(1, order.getId());
                psItem.setLong(2, coffee.getId());
                psItem.executeUpdate();
            }
        } catch (SQLException ex) {
            log.error("Failed execute save(Order)\n{}", ex.getSQLState());
        }
        return order;
    }

    public double getTotalRevenue() {
        String sql = "select sum(total_price) from orders";
        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException ex) {
            log.error("Failed execute getTotalRevenue\n{}", ex.getSQLState());
        }
        return 0;
    }

    private List<Coffee> getItemsByOrderId(Connection con, long orderId) throws SQLException {
        String sql = """
                select c.id, c.name, c.price
                from coffee c
                join order_items oi on c.id = oi.coffee_id
                where oi.order_id = ?
                """;
        List<Coffee> items = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Coffee coffee = new Coffee(rs.getString("name"), rs.getDouble("price"), 0);
                    coffee.setId(rs.getLong("id"));
                    items.add(coffee);
                }
            }
        }
        return items;
    }
}
