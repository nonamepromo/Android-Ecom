package com.example.justplay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String consoleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        consoleName = getIntent().getExtras().get("console").toString();

        Toast.makeText(this, consoleName, Toast.LENGTH_SHORT).show();
    }
}