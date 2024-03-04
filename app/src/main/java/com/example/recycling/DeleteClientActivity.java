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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;



public class DeleteClientActivity extends AppCompatActivity {

    FirebaseFirestore db;
    TextView clientInfo;
    Button delete, back;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_client);


        db = FirebaseFirestore.getInstance();
        delete = findViewById(R.id.deleteSelectedClient);
        back = findViewById(R.id.backToManageClients);
        clientInfo = findViewById(R.id.TextViewClientInfo);
        progressBar = findViewById(R.id.progressBar);
        String clientFirstName = getIntent().getStringExtra("first_name");
        String clientLastName = getIntent().getStringExtra("last_name");
        String clientUid = getIntent().getStringExtra("uid");
        String clientEmail = getIntent().getStringExtra("email");

        clientInfo.setText("First name:"+clientFirstName+"\nLast name:"+clientLastName+"\nID: "+clientUid+"\nEmail: "+clientEmail);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.setEnabled(false);
                deleteClient(clientUid);
                delete.setEnabled(true);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManageClientsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void deleteClient(String uid) {
        progressBar.setVisibility(View.VISIBLE);
            // Get a reference to the Firestore collections
            CollectionReference clientsRef = FirebaseFirestore.getInstance().collection("clients");
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");

        // Delete the client document with the specified UID
        clientsRef.document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        clientsRef.document(uid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess: client's data in clients collection has been deleted");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: client's data in clients collection is not deleted");
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
                                Log.d(TAG, "onSuccess: client's data in users collection has been deleted");
                                Toast.makeText(DeleteClientActivity.this, "Client's data has been deleted", Toast.LENGTH_SHORT).show();
                                clientInfo.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: client's data in users collection is not deleted");
                            }
                        });
                    }
                });
        progressBar.setVisibility(View.GONE);
    }
}