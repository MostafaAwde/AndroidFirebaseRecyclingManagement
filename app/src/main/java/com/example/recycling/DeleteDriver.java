package com.example.recycling;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteDriver extends AppCompatActivity {

    FirebaseFirestore db;
    TextView driverInfo;
    Button delete, back;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_driver);

        db = FirebaseFirestore.getInstance();
        delete = findViewById(R.id.deleteSelectedDriver);
        back = findViewById(R.id.backToManageDrivers);
        driverInfo = findViewById(R.id.TextViewDriverInfo);
        progressBar = findViewById(R.id.progressBar);

        String driverFirstName = getIntent().getStringExtra("first_name");
        String driverLastName = getIntent().getStringExtra("last_name");
        String driverUid = getIntent().getStringExtra("uid");
        String driverEmail = getIntent().getStringExtra("email");
        String driverCity = getIntent().getStringExtra("city");

        driverInfo.setText("City: "+driverCity+"\nFirst name:"+driverFirstName+"\nLast name:"+driverLastName+"\nID: "+driverUid+"\nEmail: "+driverEmail);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.setEnabled(false);
                deleteDriver(driverUid);
                delete.setEnabled(true);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManageDrivers.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void deleteDriver(String uid) {
        progressBar.setVisibility(View.VISIBLE);
        // Get a reference to the Firestore collections
        CollectionReference driversRef = FirebaseFirestore.getInstance().collection("drivers");
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");

        // Delete the client document with the specified UID
        driversRef.document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        driversRef.document(uid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess: Driver's data in drivers collection has been deleted");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Driver's data in drivers collection is not deleted");
                            }
                        });
                    }
                });
        usersRef.document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        usersRef.document(uid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess: Driver's data in users collection has been deleted");
                                Toast.makeText(DeleteDriver.this, "Driver's data has been deleted", Toast.LENGTH_SHORT).show();
                                driverInfo.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Driver's data in users collection is not deleted");
                            }
                        });
                    }
                });
        progressBar.setVisibility(View.GONE);
    }
}