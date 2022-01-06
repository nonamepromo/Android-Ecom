package com.example.justplay.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.justplay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminEditGameActivity extends AppCompatActivity {


    private Button applyChange, deleteGame;
    private EditText title, description, price;
    private ImageView imageView;

    private String gameId = "";
    private DatabaseReference gamesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_game);

        gameId = getIntent().getStringExtra("gId");
        gamesRef = FirebaseDatabase.getInstance().getReference().child("Videogames").child(gameId);

        applyChange = findViewById(R.id.edit_apply_changes);
        deleteGame = findViewById(R.id.delete_game);
        title = findViewById(R.id.edit_game_title);
        price = findViewById(R.id.edit_game_price);
        description = findViewById(R.id.edit_game_description);
        imageView = findViewById(R.id.edit_game_image);

        showGameInfo();

        applyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        deleteGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOneGame();
            }
        });
    }

    private void deleteOneGame() {
        gamesRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(AdminEditGameActivity.this, AdminEditGameActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(AdminEditGameActivity.this, "Videogioco eliminato con successo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyChanges() {
        String gName = title.getText().toString();
        String gPrice = price.getText().toString();
        String gDescription = description.getText().toString();

        if (gName.equals("")) {
            Toast.makeText(this, "Inserisci il titolo del videogioco", Toast.LENGTH_SHORT).show();
        }else if (gPrice.equals("")){
            Toast.makeText(this, "Inserisci il prezzo del videogioco", Toast.LENGTH_SHORT).show();
        } else if (gDescription.equals("")){
            Toast.makeText(this, "Inserisci la descrizione del videogioco", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> gameMap = new HashMap<>();
            gameMap.put("gId", gameId);
            gameMap.put("description", gDescription);
            gameMap.put("price", gPrice);
            gameMap.put("gName", gName);

            gamesRef.updateChildren(gameMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminEditGameActivity.this, "Videogioco modificato con successo", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminEditGameActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void showGameInfo() {
        gamesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String gName = snapshot.child("gName").getValue().toString();
                    String gDescription = snapshot.child("description").getValue().toString();
                    String gPrice = snapshot.child("price").getValue().toString();
                    String gImage = snapshot.child("image").getValue().toString();

                    title.setText(gName);
                    price.setText(gPrice);
                    description.setText(gDescription);
                    Picasso.get().load(gImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}