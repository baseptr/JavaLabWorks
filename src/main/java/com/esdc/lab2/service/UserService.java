package com.esdc.lab2.service;

import com.esdc.lab2.entity.Coffee;
import com.esdc.lab2.entity.User;
import com.esdc.lab2.repository.UserRepository;

import java.util.List;

public class UserService {
    public static final UserService Instance = new UserService();

    private final UserRepository userRepository = UserRepository.Instance;
    List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    User getUserById(long id) {
        return userRepository.getUser(id);
    }

    List<User> getUsersByMostOrderedCoffee(Coffee coffee) {
        return userRepository.getUsersByOrderedCoffee(coffee.getId());
    }

    public User getUserByUserName(String name) {
        return userRepository.getUserByUserName(name);
    }

    public User getOrCreateUserByUserName(String name) {
        User user = getUserByUserName(name);
        if (user == null) {
            user = new User();
            user.setName(name);
             user = userRepository.save(user);
        }
        return user;
    }

    //Optional: Get the user who ordered the most of a specific coffee
    public User getUserWhoOrderedMost(Coffee coffee) {
        return userRepository.getUserWhoOrderedMost(coffee.getId());
    }
}
