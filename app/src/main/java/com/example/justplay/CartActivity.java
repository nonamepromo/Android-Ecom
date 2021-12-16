package com.example.justplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.justplay.Model.Cart;
import com.example.justplay.Prevalent.Prevalent;
import com.example.justplay.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessBtn;
    private TextView totalAmount, confirmOrderMessage;
    private String firebaseUrl = "https://justplay-ecom-default-rtdb.europe-west1.firebasedatabase.app";
    private int grandTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextProcessBtn = (Button) findViewById(R.id.next_btn);
        totalAmount = (TextView) findViewById(R.id.total_price);
        confirmOrderMessage = (TextView) findViewById(R.id.message_confirm_order);

        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MI PORTO IL VALORE TOTALE DEL CARRELLO NELL'ORDER ACTIVITY
                Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                intent.putExtra("Prezzo Totale", String.valueOf(grandTotal));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrder();

        final DatabaseReference cartRef = FirebaseDatabase.getInstance(firebaseUrl).getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartRef.child("User View")
                        .child(Prevalent.currentOnlineUser.getUsername())
                        .child("Games"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cartModel) {
                cartViewHolder.gameTitle.setText(cartModel.getGname());
                cartViewHolder.gamePrice.setText("Prezzo " + cartModel.getPrice() + "€");
                cartViewHolder.gameConsole.setText(cartModel.getConsole());

                //SOMMO IL VALORE PER SINGOLO VIDEOGIOCO NEL CARRELLO
                grandTotal = grandTotal + ((Integer.valueOf(cartModel.getPrice())));

                //CREO UN ALERT DIALOG PER RIMUOVERE UN OGGETTO DAL CARRELLO
                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Rimuovi"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Opzioni Carrello:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0){
                                    cartRef.child("User View").child(Prevalent.currentOnlineUser.getUsername())
                                            .child("Games")
                                            .child(cartModel.getGid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(CartActivity.this, "Videogioco rimosso con successo!", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
                //MOSTRO IL PREZZO TOTALE
                totalAmount.setText("Prezzo Totale = " + String.valueOf(grandTotal) + "€");
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void checkOrder(){
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance(firebaseUrl).getReference().child("Orders").child(Prevalent.currentOnlineUser.getUsername());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String shipping = snapshot.child("state").getValue().toString();
                    String user = snapshot.child("name").getValue().toString();

                    if (shipping.equals("shipped")){
                        totalAmount.setText("Caro " + user + "\n il tuo ordine è stato spedito!");
                        recyclerView.setVisibility(View.GONE);

                        confirmOrderMessage.setVisibility(View.VISIBLE);
                        confirmOrderMessage.setText("Il tuo ordine è stato spedito con successo!");
                        nextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "Puoi effettuare nuovi acquisti solo dopo che il tuo ordine precedente viene spedito!", Toast.LENGTH_SHORT).show();
                    } else if (shipping.equals("not shipped")){
                        totalAmount.setText("Il tuo ordine non è ancora stato spedito");
                        recyclerView.setVisibility(View.GONE);

                        confirmOrderMessage.setVisibility(View.VISIBLE);
                        nextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "Puoi effettuare nuovi acquisti solo dopo che il tuo ordine precedente viene spedito!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}