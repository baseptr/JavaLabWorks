package com.esdc.lab2.repository;

import com.esdc.lab2.entity.User;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserRepository {
    public static final UserRepository Instance = new UserRepository();
    private final DataSource dataSource = DataSourceProvider.Instance.getDataSource();

    public int getTotalUsers() {
        String sql = "select count(*) from users";
        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            log.error("Failed execute getTotalUsers\n{}", ex.getSQLState());
        }
        return 0;
    }

    public List<User> getAllUsers() {
        String sql = "select id, name from users";
        List<User> res = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                res.add(user);
            }
        } catch (SQLException ex) {
            log.error("Failed execute getAllUsers\n{}", ex.getSQLState());
        }
        return res;
    }

    public User getUser(long id) {
        String sql = "select id, name from users where id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    return user;
                }
            }
        } catch (SQLException ex) {
            log.error("Failed execute getUser\n{}", ex.getSQLState());
        }
        return null;
    }

    public User getUserByUserName(String name) {
        String sql = "select id, name from users where name = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    return user;
                }
            }
        } catch (SQLException ex) {
            log.error("Failed execute getUserByUserName\n{}", ex.getSQLState());
        }
        return null;
    }

    public User save(User user) {
        String sql = "insert into users(name) values (?) returning id";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user.setId(rs.getLong("id"));
                }
            }
        } catch (SQLException ex) {
            log.error("Failed execute save(User)\n{}", ex.getSQLState());
        }
        return user;
    }

    public User getUserWhoOrderedMost(Long coffeeId) {
        String sql = """
                select u.id, u.name, count(oi.order_id) total
                from users u
                join orders o on u.id = o.user_id
                join order_items oi on o.id = oi.order_id
                where oi.coffee_id = ?
                group by u.id, u.name
                order by total desc
                limit 1
                """;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, coffeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    return user;
                }
            }
        } catch (SQLException ex) {
            log.error("Failed execute getUserWhoOrderedMost\n{}", ex.getSQLState());
        }
        return null;
    }

    public List<User> getUsersByOrderedCoffee(Long coffeeId) {
        String sql = """
                select distinct u.id, u.name
                from users u
                join orders o on u.id = o.user_id
                join order_items oi on o.id = oi.order_id
                where oi.coffee_id = ?
                """;
        List<User> res = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, coffeeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    res.add(user);
                }
            }
        } catch (SQLException ex) {
            log.error("Failed execute getUsersByOrderedCoffee\n{}", ex.getSQLState());
        }
        return res;
    }
}
