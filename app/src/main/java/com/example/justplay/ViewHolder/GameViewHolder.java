package com.example.justplay.ViewHolder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justplay.Interface.ItemClickListener;
import com.example.justplay.R;

public class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView gameTitle, gameDescription, gamePrice, gameConsole;
    public ImageView imageView;
    public CheckBox wishedGame;
    public ItemClickListener listener;

    public GameViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.game_image);
        gameTitle = (TextView) itemView.findViewById(R.id.game_title);
        gameDescription = (TextView) itemView.findViewById(R.id.game_description);
        gamePrice = (TextView) itemView.findViewById(R.id.game_price);
        gameConsole = (TextView) itemView.findViewById(R.id.game_console);
        wishedGame = (CheckBox) itemView.findViewById(R.id.add_wishlist);
    }

    public void setItemClickListener (ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAbsoluteAdapterPosition(), false);
    }
}
