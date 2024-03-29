package com.example.justplay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.justplay.Admin.AdminEditGameActivity;
import com.example.justplay.Model.Games;
import com.example.justplay.Prevalent.Prevalent;
import com.example.justplay.ViewHolder.GameViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DatabaseReference gamesRef;
    private RecyclerView recyclerView;
    private DBHandler dbHandler;
    RecyclerView.LayoutManager layoutManager;

    private String role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHandler = new DBHandler(HomeActivity.this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            role = getIntent().getExtras().get("Admin").toString();
        }

        gamesRef = FirebaseDatabase.getInstance().getReference().child("Videogames");

        //PER IL REMIND ME
        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!role.equals("Admin")){
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //MI PRENDO USERNAME UTENTE LOGGATO
        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextview = headerView.findViewById(R.id.user_profile_name);

        if (!role.equals("Admin")) {
            usernameTextview.setText(Prevalent.currentOnlineUser.getName());
        }

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //CREAIAMO LA QUERY PER PRENDERCI I VIDEOGIOCHI
        FirebaseRecyclerOptions<Games> options = new FirebaseRecyclerOptions.Builder<Games>().setQuery(gamesRef, Games.class).build();
        //PASSIAMO OPTION CHE CONTIENE LA QUERY
        FirebaseRecyclerAdapter<Games, GameViewHolder> adapter = new FirebaseRecyclerAdapter<Games, GameViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull GameViewHolder gameViewHolder, int i, @NonNull Games model) {
                gameViewHolder.gameTitle.setText(model.getGname());
                gameViewHolder.gameDescription.setText(model.getDescription());
                gameViewHolder.gameConsole.setText(model.getConsole());
                gameViewHolder.gamePrice.setText("Prezzo = " + model.getPrice() + "€");
                //LIBRERIA PER IMMAGINI
                Picasso.get().load(model.getImage()).into(gameViewHolder.imageView);
                gameViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        if (role.equals("Admin")){
                            intent = new Intent(HomeActivity.this, AdminEditGameActivity.class);
                        }else {
                            intent = new Intent(HomeActivity.this, GameDetailsActivity.class);
                        }
                        intent.putExtra("gId", model.getGid());
                        startActivity(intent);
                    }
                });
                CheckBox wishlist = gameViewHolder.wishedGame;
                String gameName = gameViewHolder.gameTitle.getText().toString();
                if (!role.equals("Admin")) {
                    if (!dbHandler.checkWished(gameName)) {
                        gameViewHolder.wishedGame.setChecked(true);
                    }
                    wishlist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!role.equals("Admin")) {

                                String gameName = gameViewHolder.gameTitle.getText().toString();
                                String gameConsole = gameViewHolder.gameConsole.getText().toString();

                                if (dbHandler.addNewFavorite(gameName, gameConsole)) {
                                    Toast.makeText(HomeActivity.this, "Gioco aggiunto alla wishlist", Toast.LENGTH_SHORT).show();
                                } else {
                                    dbHandler.deleteWishedGame(gameName);
                                    Toast.makeText(HomeActivity.this, "Gioco rimosso dalla wishlist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                } else {
                    wishlist.setVisibility(View.INVISIBLE);
                }
            }

            @NonNull
            @Override
            public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_items_layout, parent, false);
                return new GameViewHolder(view);
            }
        };
    recyclerView.setAdapter(adapter);
    adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_cart){
            if (!role.equals("Admin")) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        } else  if (id == R.id.nav_search){
            if (!role.equals("Admin")) {
                Intent intent = new Intent(HomeActivity.this, SearchGamesActivity.class);
                startActivity(intent);
            }
        } else  if (id == R.id.nav_wishlist){
            if (!role.equals("Admin")) {
                Intent intent = new Intent(HomeActivity.this, WishlistActivity.class);
                startActivity(intent);
            }
        } else  if (id == R.id.nav_settings){
            if (!role.equals("Admin")) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        }else  if (id == R.id.nav_logout){
            if (!role.equals("Admin")) {
                Paper.book().destroy();

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}