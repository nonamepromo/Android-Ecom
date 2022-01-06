package com.example.justplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputUsername, InputName, InputPhoneNumber, InputPassword;
    private ProgressDialog loadingBar;

    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputUsername = (EditText) findViewById(R.id.register_username_input);
        InputName = (EditText) findViewById(R.id.register_name_input);
        InputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String username = InputUsername.getText().toString();
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "Per favore inserisci il tuo username", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Per favore inserisci il tuo nome", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Per favore inserisci il tuo numero di telefono", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Per favore inserisci la tua password", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Crea Account");
            loadingBar.setMessage("Attendi, stiamo controllando i tuoi dati");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateUsername(username, name, phone, password);
        }
    }

    private void ValidateUsername(String username, String name, String phone, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child(parentDbName).child(username).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("username", username);
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);

                    RootRef.child("Users").child(username).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Il tuo account è stato creato con successo", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Errore di rete, per favore riprova più tardi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else{
                    Toast.makeText(RegisterActivity.this, "Questo " + username + " è già registrato", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Per favore utilizza un altro username", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}