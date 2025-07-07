package com.example.blogmusic.ui.components;


public class AuthResponse {

    public static class LoginResponse {
        private final boolean status;
        private final String message;
        private int user_id;
        private final String name;
        private final String email;
        private final String role;

        public LoginResponse(boolean status, String message, int user_id, String name, String email, String role) {
            this.status = status;
            this.message = message;
            this.user_id = user_id;
            this.name = name;
            this.email = email;
            this.role = role;
        }

        public boolean isStatus() { return status; }
        public String getMessage() { return message; }
        public int getUserId() { return user_id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getRole() {return role;}
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