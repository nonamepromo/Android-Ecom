package com.example.justplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.justplay.Model.Games;
import com.example.justplay.ViewHolder.GameViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchGamesActivity extends AppCompatActivity {

    private Button searchButton;
    private EditText inputText;
    private RecyclerView searchList;
    private String searchInput;
    private String firebaseUrl = "https://justplay-ecom-default-rtdb.europe-west1.firebasedatabase.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_games);

        inputText = findViewById(R.id.search_game_title);
        searchButton = findViewById(R.id.search_button);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchGamesActivity.this));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput = inputText.getText().toString();

                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance(firebaseUrl).getReference().child("Videogames");

        FirebaseRecyclerOptions<Games> options = new FirebaseRecyclerOptions.Builder<Games>()
                .setQuery(reference.orderByChild("gName").startAt(searchInput), Games.class)
                .build();

        FirebaseRecyclerAdapter<Games, GameViewHolder> adapter =
                new FirebaseRecyclerAdapter<Games, GameViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull GameViewHolder gameViewHolder, int i, @NonNull Games games) {
                        gameViewHolder.gameTitle.setText(games.getGname());
                        gameViewHolder.gameDescription.setText(games.getDescription());
                        gameViewHolder.gameConsole.setText(games.getConsole());
                        gameViewHolder.gamePrice.setText("Prezzo = " + games.getPrice() + "€");
                        //LIBRERIA PER IMMAGINI
                        Picasso.get().load(games.getImage()).into(gameViewHolder.imageView);

                        gameViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SearchGamesActivity.this, GameDetailsActivity.class);
                                intent.putExtra("gId", games.getGid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_items_layout, parent, false);
                        GameViewHolder holder = new GameViewHolder(view);
                        return holder;
                    }
                };
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}