package com.example.blogmusic.ui.model;

public class AuthResponse {

    // ✔️ Dùng cho login.php
    public static class LoginResponse {
        private boolean status;
        private String message;
        private String name;
        private String email;

        public boolean isStatus() { return status; }
        public String getMessage() { return message; }
        public String getName() { return name; }
        public String getEmail() { return email; }
    }

    // ✔️ Dùng cho register.php
    public static class RegisterResponse {
        private boolean status;
        private String message;

        public boolean isStatus() { return status; }
        public String getMessage() { return message; }
    }
}