package com.example.justplay.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.justplay.HomeActivity;
import com.example.justplay.MainActivity;
import com.example.justplay.R;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView ps4;
    private ImageView ps5;
    private ImageView xboxone;
    private ImageView xboxseriesx;

    private Button logoutButton, checkOrdersButton, editGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        logoutButton = (Button) findViewById(R.id.admin_logout_btn);
        checkOrdersButton = (Button) findViewById(R.id.check_order_btn);
        editGame = (Button) findViewById(R.id.edit_game);

        editGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

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