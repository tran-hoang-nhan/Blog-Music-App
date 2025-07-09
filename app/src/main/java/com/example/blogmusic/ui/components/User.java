package com.example.blogmusic.ui.components;

public class User {
    private int user_id;
    private String name;
    private String email;
    private String created_at;
    private String role;

    public User(int user_id, String name, String email, String created_at, String role) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.created_at = created_at;
        this.role = role;
    }

    // Getter và Setter
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
