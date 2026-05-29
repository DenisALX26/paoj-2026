package com.pao.project.auction_app.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.pao.project.auction_app.models.users.Bidder;
import com.pao.project.auction_app.models.users.Seller;
import com.pao.project.auction_app.models.users.User;
import com.pao.project.auction_app.repository.UserRepository;

public class UserService {
    private static UserService instance;
    private final UserRepository userRepository = new UserRepository();
    private final Map<String, User> userCache = new HashMap<>();

    private UserService() {
        loadUsersFromDatabase();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void signUp(String type, String userName, String email, String password) throws Exception {
        if (userCache.containsKey(email)) {
            throw new Exception("Email already in use");
        }

        User newUser;
        switch (type.toLowerCase()) {
            case "seller":
                newUser = new Seller(userName, email, password);
                break;
            case "bidder":
                newUser = new Bidder(userName, email, password);
                break;
            default:
                throw new IllegalArgumentException("Invalid user type: " + type);
        }

        userCache.put(email, newUser);
        
        userRepository.save(newUser);
    }

    public User login(String email, String password) throws Exception {
        User user = userCache.get(email);
        if (user == null || !user.getPassword().equals(password)) {
            throw new Exception("Invalid email or password");
        }
        return user;
    }

    private void loadUsersFromDatabase() {
        for (User user : userRepository.findAll()) {
            userCache.put(user.getEmail(), user);
        }
    }

    public User getUserByEmail(String email) {
        return userCache.get(email);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }
}