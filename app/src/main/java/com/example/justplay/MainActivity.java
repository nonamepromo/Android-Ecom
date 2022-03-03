package com.example.justplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.justplay.Model.Users;
import com.example.justplay.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button joinNowButton, loginButton;
    private ProgressDialog loadingBar;

    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton = (Button) findViewById(R.id.main_join_now_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserUsernameKey = Paper.book().read(Prevalent.UserUsernameKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if(UserUsernameKey != "" && UserPasswordKey != ""){
            if(!TextUtils.isEmpty(UserUsernameKey) && !TextUtils.isEmpty(UserPasswordKey)){
                AllowAccess(UserUsernameKey, UserPasswordKey);

                loadingBar.setTitle("Login già effettuato");
                loadingBar.setMessage("Per favore attendi");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    private void AllowAccess(final String username, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(username).exists()){
                    Users usersData = snapshot.child(parentDbName).child(username).getValue(Users.class);

                    if(usersData.getUsername().equals(username)){
                        if(usersData.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this, "Login effettuato con successo", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            //QUESTO RISOLVE IL PROBLEMA CHE QUANDO SI SELEZIONAVA "RICORDATI DI ME", AL SECONDO ACCESSO ALL'APP
                            //L'APPLICAZIONE CRASHAVA PERCHE' NON RIUSCIVA A PRENDERSI L'USERNAME DELL'UTENTE LOGGATO DA FARLO VISUALIZZARE NELLA TOOLBAR
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                        }else {
                            Toast.makeText(MainActivity.this, "Password Errata", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                }else {
                    Toast.makeText(MainActivity.this, "L'account con questo username: " + username + " non esiste", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}