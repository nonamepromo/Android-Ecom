package com.example.justplay.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.justplay.Model.AdminOrders;
import com.example.justplay.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef, AdminOrders.class).build();
        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder adminOrdersViewHolder, @SuppressLint("RecyclerView") final int i, @NonNull AdminOrders adminOrdersModel) {
                adminOrdersViewHolder.userName.setText("Nome: " + adminOrdersModel.getName());
                adminOrdersViewHolder.userSurname.setText("Cognome: " + adminOrdersModel.getSurname());
                adminOrdersViewHolder.userPhone.setText("Numero di telefono: " + adminOrdersModel.getPhone());
                adminOrdersViewHolder.userTotal.setText("Totale = " + adminOrdersModel.getTotalAmount() + "€");
                adminOrdersViewHolder.userDateTime.setText("Data e ora: " + adminOrdersModel.getDate() + " " + adminOrdersModel.getTime());
                adminOrdersViewHolder.userShipping.setText("Indirizzo di spedizione: " + adminOrdersModel.getAddress() + ", " + adminOrdersModel.getCity());

                adminOrdersViewHolder.showOrdersButton.setOnClickListener(v -> {
                    String uId = getRef(i).getKey();
                    Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserGamesActivity.class);
                    //PRENDIAMO ID DELL'UTENTE E LO PASSIAMO POI NELL'ACTIVITI ADMIN USER GAMES, CHE POTRà RICHIAMARE QUESTA STRINGA UTILIZZANDO GETINTENT DI "UID" CHE è COME LO ABBIAMO CHIAMATO
                    intent.putExtra("uid", uId);
                    startActivity(intent);
                });

                adminOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Si",
                                "No"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        builder.setTitle("Hai spedito l'ordine?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (i == 0){
                                    String uId = getRef(i).getKey();
                                    
                                    removeOrder(uId);
                                }else {
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                return new AdminOrdersViewHolder(view);
            }
        };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeOrder(String uId) {
        ordersRef.child(uId).removeValue();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{
        public TextView userName, userSurname, userPhone, userTotal, userDateTime, userShipping;
        public Button showOrdersButton;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userSurname = itemView.findViewById(R.id.order_user_surname);
            userPhone = itemView.findViewById(R.id.order_phone_number);
            userTotal = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShipping = itemView.findViewById(R.id.order_address_city);
            showOrdersButton = itemView.findViewById(R.id.show_all_products);
        }
    }
}