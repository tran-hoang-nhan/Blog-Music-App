package com.example.blogmusic.ui.components;

public class Order {
    private final int id;
    private final int user_id;
    private final int album_id;
    private final String album_title;
    private final String name;
    private final String address;
    private final String phone;
    private final int quantity;
    private final String order_date;
    private final String status;
    private final String role;

    public Order(int id, int user_id, int album_id, String album_title, String name, String address, String phone, int quantity, String orderDate, String status, String role) {
        this.id = id;
        this.user_id = user_id;
        this.album_id = album_id;
        this.album_title = album_title;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.quantity = quantity;
        this.order_date = orderDate;
        this.status = status;
        this.role = role;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getAlbum_title() {
        return album_title;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
    public String getRole() { return role; }
}
