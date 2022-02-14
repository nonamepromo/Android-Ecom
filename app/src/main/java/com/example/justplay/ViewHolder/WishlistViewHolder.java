package com.example.justplay.ViewHolder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justplay.DBHandler;
import com.example.justplay.HomeActivity;
import com.example.justplay.Model.Wished;
import com.example.justplay.R;

import java.util.ArrayList;

public class WishlistViewHolder extends RecyclerView.Adapter<WishlistViewHolder.ViewHolder> {

    // variable for our array list and context
    private ArrayList<Wished> wishedModalArrayList;
    private Context context;
    private DBHandler dbHandler;

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

        dbHandler = new DBHandler(context);

        //Da sistemare
        String wishedName = modal.getGameName();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                        "Rimuovi"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Opzioni Wishlist:");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (i == 0){
                            dbHandler.deleteWishedGame(wishedName);
                            Toast.makeText(context, "Videogioco eliminato", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, HomeActivity.class);
                            context.startActivity(intent);
                        }
                    }
                });builder.show();
            }
        });

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