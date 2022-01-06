package com.example.justplay.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.justplay.Model.Cart;
import com.example.justplay.R;
import com.example.justplay.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserGamesActivity extends AppCompatActivity {

    private RecyclerView videogamesList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_games);
        //QUI CI PRENDIAMO "UID" CHE ABBIAMO CREATO IN ADMIN NEW ORDERS
        userID = getIntent().getStringExtra("uid");

        videogamesList = findViewById(R.id.videogames_list);
        videogamesList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        videogamesList.setLayoutManager(layoutManager);

        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(userID).child("Games");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cartModel) {
                cartViewHolder.gameTitle.setText(cartModel.getGname());
                cartViewHolder.gamePrice.setText("Prezzo " + cartModel.getPrice() + "€");
                cartViewHolder.gameConsole.setText(cartModel.getConsole());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;            }
        };
        videogamesList.setAdapter(adapter);
        adapter.startListening();
    }
}