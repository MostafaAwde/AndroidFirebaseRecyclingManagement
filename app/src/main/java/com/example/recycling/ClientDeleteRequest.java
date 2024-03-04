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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClientDeleteRequest extends AppCompatActivity {

    TextView requestInfo;

    Button delete, back;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_delete_request);

        delete = findViewById(R.id.deleteSelectedRequest);
        back = findViewById(R.id.btnBackToViewRequests);
        requestInfo = findViewById(R.id.TextViewRequestInfo);
        progressBar = findViewById(R.id.progressBar);

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
                Intent intent = new Intent(getApplicationContext(), ClientRequests.class);
                startActivity(intent);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.setEnabled(false);
                deleteRequest(request_id);
                Intent intent = new Intent(getApplicationContext(), ClientRequests.class);
                startActivity(intent);
                delete.setEnabled(true);
                finish();
            }
        });

    }
    private void deleteRequest(String uid) {
        progressBar.setVisibility(View.VISIBLE);

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
        progressBar.setVisibility(View.GONE);
    }
}