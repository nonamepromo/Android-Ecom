package com.example.justplay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.justplay.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private EditText nameEdit, phoneEdit;
    private TextView saveButton, closeButton;

    private String checkClick= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        nameEdit = (EditText) findViewById(R.id.edit_name);
        phoneEdit = (EditText) findViewById(R.id.edit_phone);
        closeButton = (TextView) findViewById(R.id.close_settings_btn);
        saveButton = (TextView) findViewById(R.id.update_settings_btn);

        userInfoView(nameEdit, phoneEdit);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkClick.equals("cliecked")) {
                    saveUserInfo();
                } else{
                    updateUserInfo();
                }
            }
        });
    }

    private void updateUserInfo() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Aggiornamento profilo");
        progressDialog.setMessage("Per favore attendi mentre aggiorniamo le tue informazioni");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", nameEdit.getText().toString());
        userMap.put("phone", phoneEdit.getText().toString());
        reference.child(Prevalent.currentOnlineUser.getUsername()).updateChildren(userMap);

        progressDialog.dismiss();

        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Le tue informazioni sono state aggiornate con successo!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void saveUserInfo() {
        if (TextUtils.isEmpty(nameEdit.getText().toString())){
            Toast.makeText(this, "Inserisci il nome", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phoneEdit.getText().toString())){
            Toast.makeText(this, "Inserisci il numero di telefono", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoView(final EditText nameEdit, final EditText phoneEdit) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getUsername());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();
                    String phone = snapshot.child("phone").getValue().toString();

                    nameEdit.setText(name);
                    phoneEdit.setText(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}