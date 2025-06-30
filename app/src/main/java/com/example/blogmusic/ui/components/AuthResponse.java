package com.example.blogmusic.ui.components;


public class AuthResponse {

    public static class LoginResponse {
        private final boolean status;
        private final String message;
        private final int user_id;
        private final String name;
        private final String email;

        public LoginResponse(boolean status, String message, int userId, String name, String email) {
            this.status = status;
            this.message = message;
            user_id = userId;
            this.name = name;
            this.email = email;
        }

        public boolean isStatus() { return status; }
        public String getMessage() { return message; }
        public int getUserId() { return user_id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
    }


    public static class RegisterResponse {
        private final boolean status;
        private final String message;

        public RegisterResponse(boolean status, String message) {
            this.status = status;
            this.message = message;
        }

        public boolean isStatus() { return status; }
        public String getMessage() { return message; }
    }
}