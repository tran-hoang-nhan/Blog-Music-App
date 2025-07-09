package com.example.blogmusic.ui.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogmusic.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList = new ArrayList<>();
    private final OnUserDeleteListener deleteListener;

    public interface OnUserDeleteListener {
        void onUserDelete(User user);
    }

    public UserAdapter(OnUserDeleteListener listener) {
        this.deleteListener = listener;
    }

    public void setUserList(List<User> list) {
        this.userList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvName.setText(user.getName());
        holder.tvEmail.setText(user.getEmail());
        holder.tvRole.setText("Vai trò: " + user.getRole());
        holder.tvDate.setText("Ngày tạo: " + user.getCreated_at());



        int bgColor = user.getRole().equalsIgnoreCase("admin")
                ? ContextCompat.getColor(holder.itemView.getContext(), R.color.card_admin)
                : ContextCompat.getColor(holder.itemView.getContext(), R.color.card_user);
        holder.setCardBackgroundColor(bgColor);

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onUserDelete(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvRole, tvDate;
        Button btnDelete;
        CardView cardView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_user_name);
            tvEmail = itemView.findViewById(R.id.tv_user_email);
            tvRole = itemView.findViewById(R.id.tv_user_role);
            tvDate = itemView.findViewById(R.id.tv_user_date);
            btnDelete = itemView.findViewById(R.id.btn_delete_user);
            cardView = itemView.findViewById(R.id.card_user);

        }
        public void setCardBackgroundColor(int color) {
            cardView.setCardBackgroundColor(color);
        }
    }
}

