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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ManageRequests extends AppCompatActivity {

    TextView requestInfo;
    Button delete, back, addRequestToTasks;
    ProgressBar progressBar, progressBar1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests);

        delete = findViewById(R.id.deleteSelectedRequest);
        back = findViewById(R.id.backToViewRequests);
        addRequestToTasks = findViewById(R.id.btnAddRequestToTasks);
        requestInfo = findViewById(R.id.TextViewRequestInfo);
        progressBar = findViewById(R.id.progressBar);
        progressBar1 = findViewById(R.id.progressBar1);

        String city = getIntent().getStringExtra("city");
        String client_first_name = getIntent().getStringExtra("client_first_name");
        String client_id = getIntent().getStringExtra("client_id");
        String client_last_name = getIntent().getStringExtra("client_last_name");
        String material_type = getIntent().getStringExtra("material_type");
        String quantity_requested = getIntent().getStringExtra("quantity_requested");
        String request_id = getIntent().getStringExtra("request_id");

        requestInfo.setText("City: "+city+"\nclient_first_name: "+client_first_name+"\nclient_last_name: "+client_last_name+"\nclient_id: "+client_id+"\nmaterial_type: "+material_type+"\nquantity_requested: "+quantity_requested+"\nrequest_id: "+request_id);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewRequests.class);
                startActivity(intent);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.setEnabled(false);
                deleteRequest(request_id);
                Intent intent = new Intent(getApplicationContext(), ViewRequests.class);
                startActivity(intent);
                delete.setEnabled(true);
                finish();
            }
        });

        addRequestToTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRequestToTasks.setEnabled(false);
                moveRequestToTasks(request_id, city, client_first_name, client_id, client_last_name, material_type, quantity_requested);
                addRequestToTasks.setEnabled(true);
            }
        });
    }

    private void deleteRequest(String uid) {
        progressBar1.setVisibility(View.VISIBLE);

        // Get a reference to the Firestore collection
        CollectionReference requestsRef = FirebaseFirestore.getInstance().collection("requests");

        // Delete the request document with the specified UID
        requestsRef.document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        requestsRef.document(uid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                requestInfo.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Request is not deleted");
                            }
                        });
                    }
                });
        progressBar1.setVisibility(View.GONE);
    }
    private void moveRequestToTasks(String requestId, String city, String clientFirstName, String clientId, String clientLastName, String materialType, String quantityRequested) {
        progressBar.setVisibility(View.VISIBLE);

        // Get references to the Firestore collections
        CollectionReference requestsRef = FirebaseFirestore.getInstance().collection("requests");
        CollectionReference tasksRef = FirebaseFirestore.getInstance().collection("tasks");
        CollectionReference driversRef = FirebaseFirestore.getInstance().collection("drivers");

        String lowercaseCity = city.toLowerCase();

        // Find a driver with matching city
        driversRef.whereEqualTo("city", lowercaseCity)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Get the driver document
                            DocumentSnapshot driverSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String driverId = driverSnapshot.getId();

                            // Create a new task document in the tasks collection
                            Task task = new Task(city, clientFirstName, clientId, clientLastName, materialType, quantityRequested, driverId,"Incomplete", requestId);
                            tasksRef.document(requestId)
                                    .set(task)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Task document created successfully, now delete the request document
                                            Toast.makeText(ManageRequests.this, "Request has been added to Tasks", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ManageRequests.this, "Failed to create task", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(ManageRequests.this, "No available drivers for the city", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ManageRequests.this, "Failed to fetch drivers", Toast.LENGTH_SHORT).show();
                    }
                });
        progressBar.setVisibility(View.GONE);
    }
}