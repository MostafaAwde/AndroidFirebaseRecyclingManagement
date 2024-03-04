package com.example.recycling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class ClientSendRequestActivity extends AppCompatActivity {

    TextInputEditText editCity, editTextFirstName, editTextLastName, editTextMaterialType, editTextQuantity;
    Button buttonSendRequest, back;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_send_request);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editCity = findViewById(R.id.city);
        editTextFirstName = findViewById(R.id.client_first_name);
        editTextLastName = findViewById(R.id.client_last_name);
        editTextMaterialType = findViewById(R.id.material_type);
        editTextQuantity = findViewById(R.id.quantity_requested);
        buttonSendRequest = findViewById(R.id.btnSendRequest);
        back = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);

        buttonSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClientRequests.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendRequest() {
        String city = editCity.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String materialType = editTextMaterialType.getText().toString().trim();
        String quantityRequested = editTextQuantity.getText().toString().trim();

        // Generate a unique request ID
        String requestId = UUID.randomUUID().toString();

        // Get the current user's ID
        String clientId = mAuth.getCurrentUser().getUid();

        // Create a new request object
        Request request = new Request(city, firstName, clientId, lastName, materialType, quantityRequested, requestId);

        // Get a reference to the Firestore collection
        CollectionReference requestsRef = db.collection("requests");

        // Add the request to the Firestore collection
        requestsRef.document(requestId)
                .set(request)
                .addOnSuccessListener(aVoid -> {
                    // Request sent successfully
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Request has been send", Toast.LENGTH_SHORT).show();
                    editCity.setText("");
                    editTextFirstName.setText("");
                    editTextLastName.setText("");
                    editTextMaterialType.setText("");
                    editTextQuantity.setText("");
                })
                .addOnFailureListener(e -> {
                    // Error occurred while sending request
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Failed to send request", Toast.LENGTH_SHORT).show();
                });

        // Show progress bar while sending request
        progressBar.setVisibility(View.VISIBLE);
    }
}