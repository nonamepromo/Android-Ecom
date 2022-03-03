package com.example.justplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.justplay.Model.Games;
import com.example.justplay.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class GameDetailsActivity extends AppCompatActivity {

    private Button addToCartButton;
    private android.widget.ImageView gameImage;
    private TextView gamePrice, gameDescription, gameTitle, gameConsole;
    private String gameId = "", state = "Free";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        gameId = getIntent().getStringExtra("gId");

        addToCartButton = (Button) findViewById(R.id.game_add_to_cart);
        gameImage = (ImageView) findViewById(R.id.game_image_details);
        gamePrice = (TextView) findViewById(R.id.game_price_details);
        gameDescription = (TextView) findViewById(R.id.game_description_details);
        gameTitle = (TextView) findViewById(R.id.game_title_details);
        gameConsole = (TextView) findViewById(R.id.game_console_details);

        getGameDetails(gameId);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state.equals("Ordine Registrato") || state.equals("Ordine Spedito")){
                    Toast.makeText(GameDetailsActivity.this, "Puoi comprare altri prodotti solo dopo che il tuo ordine viene spedito!", Toast.LENGTH_LONG).show();
                } else {
                    addToCartList();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrder();
    }

    private void addToCartList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("gId", gameId);
        cartMap.put("gName", gameTitle.getText().toString());
        cartMap.put("price", gamePrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("discount", "");
        cartMap.put("console",gameConsole.getText().toString());

        cartRef.child("User View").child(Prevalent.currentOnlineUser.getUsername()).child("Games").child(gameId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    cartRef.child("Admin View").child(Prevalent.currentOnlineUser.getUsername()).child("Games").child(gameId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(GameDetailsActivity.this, "Aggiunto al carrello", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(GameDetailsActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

    }

    private void getGameDetails(String gameId) {
        DatabaseReference gamesRef = FirebaseDatabase.getInstance().getReference().child("Videogames");


        gamesRef.child(gameId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Games games = snapshot.getValue(Games.class);

                    gameTitle.setText(games.getGname());
                    gamePrice.setText(games.getPrice() + "€");
                    gameDescription.setText(games.getDescription());
                    gameConsole.setText(games.getConsole());
                    Picasso.get().load(games.getImage()).into(gameImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkOrder(){
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getUsername());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String shipping = snapshot.child("state").getValue().toString();

                    if (shipping.equals("shipped")){
                        state = "Ordine Spedito";
                    } else if (shipping.equals("not shipped")){
                        state = "Ordine Registrato";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}