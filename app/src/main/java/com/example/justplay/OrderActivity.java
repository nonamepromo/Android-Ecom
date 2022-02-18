package com.example.justplay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.justplay.Admin.AdminAddNewProductActivity;
import com.example.justplay.Prevalent.Prevalent;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private Button confirmOrder, selectPosition, getPosition;
    private String totalAmount = "";
    private static final int MAPS_PERMISSION_CODE = 1;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        totalAmount = getIntent().getStringExtra("Prezzo Totale");
        Toast.makeText(this, "Prezzo Totale = " + totalAmount + "€", Toast.LENGTH_SHORT).show();

        selectPosition = (Button) findViewById(R.id.select_position);
        getPosition = (Button) findViewById(R.id.get_position);
        confirmOrder = (Button) findViewById(R.id.confirm_order_btn);
        nameEditText = (EditText) findViewById(R.id.shipment_name);
        phoneEditText = (EditText) findViewById(R.id.shipment_phone);
        addressEditText = (EditText) findViewById(R.id.shipment_address);
        cityEditText = (EditText) findViewById(R.id.shipment_city);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Places.initialize(getApplicationContext(),"AIzaSyB_qaMUWfiUBQfxV4jJ2Q71RAQJWW6jOLY");
        addressEditText.setFocusable(false);
        addressEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(OrderActivity.this);
                startActivityForResult(intent,100);
            }
        });

        selectPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenMaps();
            }
        });

        getPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMapsPosition();
            }
        });

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    private void getMapsPosition() {
        if (ContextCompat.checkSelfPermission(OrderActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        try {
                        Geocoder geocoder = new Geocoder(OrderActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        addressEditText.setText(addresses.get(0).getAddressLine(0));
                        cityEditText.setText(addresses.get(0).getLocality() + " " + addresses.get(0).getPostalCode());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        } else {
            requestMapsPermission();
        }
    }

    private void requestMapsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this).setTitle("Permesso richiesto").setMessage("Questo permesso è necessario per riempire i campi per la spedizione utilizzando la tua posizione")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(OrderActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MAPS_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MAPS_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MAPS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                OpenMaps();
            } else {
                Toast.makeText(this, "Permesso negato", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void OpenMaps() {
        if (ContextCompat.checkSelfPermission(OrderActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(OrderActivity.this, MapsActivity.class);
            startActivity(intent);
        } else {
            requestMapsPermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            LatLng latLng = place.getLatLng();
            double MyLat = latLng.latitude;
            double MyLong = latLng.longitude;
            Geocoder geocoder = new Geocoder(OrderActivity.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
                addressEditText.setText(place.getName());
                cityEditText.setText(addresses.get(0).getLocality() + " " + addresses.get(0).getPostalCode());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR){
            Toast.makeText(getApplicationContext(), "Si è presentato un errore, ritenta più tardi", Toast.LENGTH_SHORT).show();
        }
        
    }

    private void check() {
        if (TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this, "Per favore inserisci il tuo nome", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phoneEditText.getText().toString())){
            Toast.makeText(this, "Per favore inserisci il tuo numero di telefono", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this, "Per favore inserisci il tuo indirizzo di spedizione", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cityEditText.getText().toString())){
            Toast.makeText(this, "Per favore inserisci la tua città", Toast.LENGTH_SHORT).show();
        } else {
            createOrder();
        }
    }

    private void createOrder() {
        final String saveCurrentTime, saveCurrentDate;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getUsername());

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("address", addressEditText.getText().toString());
        ordersMap.put("city", cityEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "not shipped");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //UNA VOLTA COMPLETATO L'ORDINE IL CARRELLO VA SVUOTATO, QUINDI IN CART LIST MI PRENDO LA USER VIEW CON
                    // IL NUMERO UGUALE A QUELLO DELL'UTENTE LOGGATO CHE STA CONFERMANDO L'ORDINE (PER QUESTO SI USA LA REMOVE)
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List").child("User View")
                            .child(Prevalent.currentOnlineUser.getUsername())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(OrderActivity.this, "Il tuo ordine è stato effettuato con successo", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(OrderActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }
}