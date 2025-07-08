package com.example.blogmusic.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.blogmusic.R;
import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.network.RetrofitClient;
import com.example.blogmusic.ui.components.Order;
import com.example.blogmusic.ui.components.OrderResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {
    private LinearLayout orderContainer;
    private TextView emptyOrderText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        orderContainer = view.findViewById(R.id.order_container);
        emptyOrderText = view.findViewById(R.id.tv_empty_order);

        SharedPreferences prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        int user_id = prefs.getInt("userId", 0);
        String role = prefs.getString("role", "user");

        if (role.equals("admin")) {
            user_id = 0;
        }
        fetchOrders(user_id);
        return view;
    }

    private void fetchOrders(int userId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<OrderResponse> call = apiService.getOrders(userId);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponse> call, @NonNull Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    List<Order> orders = response.body().getData();
                    LayoutInflater inflater = LayoutInflater.from(getContext());

                    if (orders.isEmpty()) {
                        emptyOrderText.setVisibility(View.VISIBLE);
                    } else {
                        emptyOrderText.setVisibility(View.GONE);
                        for (Order order : orders) {
                            View orderView = inflater.inflate(R.layout.item_order, orderContainer, false);
                            ((TextView) orderView.findViewById(R.id.tv_user_id)).setText("User ID: " + order.getUser_id());
                            ((TextView) orderView.findViewById(R.id.tv_user_name)).setText("Tên người dùng: " + order.getName());
                            ((TextView) orderView.findViewById(R.id.tv_album_name)).setText("Album: " + order.getAlbum_title());
                            ((TextView) orderView.findViewById(R.id.tv_order_phone)).setText("SĐT: " + order.getPhone());
                            ((TextView) orderView.findViewById(R.id.tv_order_quantity)).setText("Số lượng: " + order.getQuantity());
                            ((TextView) orderView.findViewById(R.id.tv_order_date)).setText("Ngày đặt: " + order.getOrder_date());
                            ((TextView) orderView.findViewById(R.id.tv_order_status)).setText("Trạng thái: " + order.getStatus());

                            orderContainer.addView(orderView);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Không có đơn hàng nào", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

