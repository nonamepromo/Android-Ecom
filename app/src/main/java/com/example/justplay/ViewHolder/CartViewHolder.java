package com.example.justplay.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justplay.Interface.ItemClickListener;
import com.example.justplay.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView gameTitle, gamePrice, gameConsole;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        gameTitle = itemView.findViewById(R.id.cart_game_title);
        gamePrice = itemView.findViewById(R.id.cart_game_price);
        gameConsole = itemView.findViewById(R.id.cart_game_console);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAbsoluteAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
