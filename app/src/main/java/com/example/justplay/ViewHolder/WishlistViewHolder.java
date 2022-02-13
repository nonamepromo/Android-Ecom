package com.example.justplay.ViewHolder;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justplay.Model.Wished;
import com.example.justplay.R;
import com.example.justplay.WishlistActivity;

import java.util.ArrayList;

public class WishlistViewHolder extends RecyclerView.Adapter<WishlistViewHolder.ViewHolder> {

    // variable for our array list and context
    private ArrayList<Wished> wishedModalArrayList;
    private Context context;

    public WishlistViewHolder(ArrayList<Wished> wishedModalArrayList, Context context) {
        this.wishedModalArrayList = wishedModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // on below line we are inflating our layout file for our recycler view items.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_items_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wished modal = wishedModalArrayList.get(position);
        holder.gameName.setText(modal.getGameName());
        holder.gameConsole.setText(modal.getGameConsole());
        holder.gamePrice.setText(modal.getGamePrice());


    }

    @Override
    public int getItemCount() {
        // returning the size of our array list
        return wishedModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our text views.
        private TextView gameName, gameConsole, gamePrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views

            gameName = itemView.findViewById(R.id.wishlist_game_title);
            gameConsole = itemView.findViewById(R.id.wishlist_game_console);
            gamePrice = itemView.findViewById(R.id.wishlist_game_price);
        }
    }
}