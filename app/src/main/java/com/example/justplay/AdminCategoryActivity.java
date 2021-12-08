package com.example.justplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView ps4;
    private ImageView ps5;
    private ImageView xboxone;
    private ImageView xboxseriesx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        ps4 = (ImageView) findViewById(R.id.ps4);
        ps5 = (ImageView) findViewById(R.id.ps5);
        xboxone = (ImageView) findViewById(R.id.xboxone);
        xboxseriesx = (ImageView) findViewById(R.id.xboxseriesx);

        ps4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("console","PS4");
                startActivity(intent);
            }
        });

        ps5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("console", "PS5");
                startActivity(intent);
            }
        });

        xboxone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("console", "Xbox One");
                startActivity(intent);
            }
        });

        xboxseriesx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("console", "Xbox Series X");
                startActivity(intent);
            }
        });
    }
}