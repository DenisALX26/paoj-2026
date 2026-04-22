package com.pao.project.auction_app.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import com.pao.project.auction_app.models.users.Bidder;
import com.pao.project.auction_app.models.users.Seller;
import com.pao.project.auction_app.models.users.User;

public class UserService {
    private static UserService instance;
    private static final String FILE_PATH = "src/com/pao/project/auction_app/data/users.csv";
    private final Map<String, User> userCache = new HashMap<>();

    private UserService() {
        loadUsersFromFile();
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
        saveUserToFile(newUser);
    }

    public User login(String email, String password) throws Exception {
        User user = userCache.get(email);
        if (user == null || !user.getPassword().equals(password)) {
            throw new Exception("Invalid email or password");
        }
        return user;
    }

    private void saveUserToFile(User user) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, true)))) {
            String extra = (user instanceof Bidder) ? String.valueOf(((Bidder) user).getBalance()) : "0";

            out.println(String.format("%s|%s|%s|%s|%s|%s", user.getAccountType(),
                    user.getId(), user.getUserName(), user.getEmail(), user.getPassword(), extra));
        } catch (IOException e) {
            System.err.println("Error saving user to file: " + e.getMessage());
        }
    }

    private void loadUsersFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");

                String type = parts[0];
                UUID id = UUID.fromString(parts[1]);
                String userName = parts[2];
                String email = parts[3];
                String password = parts[4];
                double extra = Double.parseDouble(parts[5]);

                User user;
                if (type.equalsIgnoreCase("Seller")) {
                    user = new Seller(id, userName, email, password);
                    userCache.put(email, user);
                } else if (type.equalsIgnoreCase("Bidder")) {
                    user = new Bidder(id, userName, email, password, extra);
                    userCache.put(email, user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users from file: " + e.getMessage());
        }
    }

    public User getUserByEmail(String email) {
        return userCache.get(email);
    }

    public User getUserById(UUID id) {
        for (User user : userCache.values()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
}
