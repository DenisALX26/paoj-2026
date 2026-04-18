package com.pao.project.auction_app.models.users;

import java.util.UUID;

public abstract class User {
    private final UUID id;
    protected String userName;
    protected String email;
    protected String password;

    public User(String userName, String email, String password) {
        this.id = UUID.randomUUID();
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public User(UUID id, String userName, String email, String password) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [userName=" + userName + ", email=" + email + "]";
    }

    public abstract String getAccountType();
}
