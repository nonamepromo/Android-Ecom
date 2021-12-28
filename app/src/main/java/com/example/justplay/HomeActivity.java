package com.example.justplay;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

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

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DatabaseReference gamesRef;
    private RecyclerView recyclerView;
    private String firebaseUrl = "https://justplay-ecom-default-rtdb.europe-west1.firebasedatabase.app";
    RecyclerView.LayoutManager layoutManager;

    private String role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            role = getIntent().getExtras().get("Admin").toString();
        }

        gamesRef = FirebaseDatabase.getInstance(firebaseUrl).getReference().child("Videogames");

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

        //MI PRENDO USERNAME UTENTE LOGGATO E IMMAGINE PER MOSTRARLO
        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextview = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

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
                        if (role.equals("Admin")){
                            Intent intent = new Intent(HomeActivity.this, AdminEditGameActivity.class);
                            intent.putExtra("gId", model.getGid());
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(HomeActivity.this, GameDetailsActivity.class);
                            intent.putExtra("gId", model.getGid());
                            startActivity(intent);
                        }
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
    recyclerView.setAdapter(adapter);
    adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
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