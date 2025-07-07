package com.example.blogmusic.ui.components;

import java.util.List;

public class OrderResponse {
    private final boolean status;
    private final String message;
    private final List<Order> data;

    public OrderResponse(boolean status, String message, List<Order> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public boolean isStatus() { return status; }
    public String getMessage() { return message; }
    public List<Order> getData() { return data; }
} 