package com.example.justplay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.justplay.Model.Wished;
import com.example.justplay.ViewHolder.WishlistViewHolder;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Wished> wishedModalArrayList;
    private DBHandler dbHandler;
    private WishlistViewHolder wishlistViewHolder;
    private TextView closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        closeButton = (TextView) findViewById(R.id.close_wishlist_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // initializing our all variables.
        wishedModalArrayList = new ArrayList<>();
        dbHandler = new DBHandler(WishlistActivity.this);

        // getting our course array list from db handler class.
        wishedModalArrayList = dbHandler.readWishedGame();

        // on below line passing our array lost to our adapter class.
        wishlistViewHolder = new WishlistViewHolder(wishedModalArrayList, WishlistActivity.this);
        recyclerView = findViewById(R.id.wishlist_list);

        // setting layout manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WishlistActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        // setting our adapter to recycler view.
        recyclerView.setAdapter(wishlistViewHolder);
    }
}