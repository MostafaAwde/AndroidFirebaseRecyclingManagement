package com.example.recycling;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ManageDrivers extends AppCompatActivity {

    CollectionReference driversRef;
    Button addDriver, back;
    ListView ls;
    ArrayAdapter<Driver> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_drivers);

        driversRef = FirebaseFirestore.getInstance().collection("drivers");

        addDriver = findViewById(R.id.btnAddNewDriver);
        back = findViewById(R.id.btnBackToManagerHome);

        ls = findViewById(R.id.listViewDrivers);

        adapter =  new ArrayAdapter<Driver>(this, android.R.layout.simple_list_item_1, new ArrayList<Driver>());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Manager_home_page.class);
                startActivity(intent);
                finish();
            }
        });

        driversRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Driver> driversList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Driver driver = document.toObject(Driver.class);
                    driversList.add(driver);
                }
                adapter.clear();
                adapter.addAll(driversList);
                Log.d(TAG, "adapter size: "+adapter.getCount());

                Log.d(TAG, "Driver List Size: " + driversList.size());
                for (Driver driver : driversList) {
                    Log.d(TAG, "Driver: " + driver.getFirst_name() + " " + driver.getLast_name() + " - ID: " + driver.getUid());
                }
            } else {
                Log.d(TAG, "Error getting drivers: ", task.getException());
            }
        });
        ls.setAdapter(adapter);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Driver selectedDriver = adapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), DeleteDriver.class);

                intent.putExtra("city", selectedDriver.getCity());
                intent.putExtra("first_name", selectedDriver.getFirst_name());
                intent.putExtra("last_name", selectedDriver.getLast_name());
                intent.putExtra("uid", selectedDriver.getUid());
                intent.putExtra("email", selectedDriver.getEmail());

                startActivity(intent);
                finish();
            }
        });

        addDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDriver.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(), AddDriver.class);
                startActivity(intent);
                finish();
                addDriver.setEnabled(true);
            }
        });
    }
}