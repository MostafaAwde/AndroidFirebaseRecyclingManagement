package com.example.recycling;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddClient extends AppCompatActivity {

    TextInputEditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword, editTextConfirmPassword;
    Button submit, back;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseFirestore db;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        submit = findViewById(R.id.btnSubmitNewClient);
        back = findViewById(R.id.btnBackToManageClients);
        editTextFirstName = findViewById(R.id.addFirst_name);
        editTextLastName = findViewById(R.id.addLast_name);
        editTextEmail = findViewById(R.id.addEmail);
        editTextPassword = findViewById(R.id.addPassword);
        editTextConfirmPassword = findViewById(R.id.addConfirmPassword);
        progressBar = findViewById(R.id.progressBar);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setEnabled(false);
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String role = "client";
                // Validate input fields
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(AddClient.this, "Enter email", Toast.LENGTH_SHORT).show();
                    submit.setEnabled(true);
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(AddClient.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                    submit.setEnabled(true);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(AddClient.this, "Enter password", Toast.LENGTH_SHORT).show();
                    submit.setEnabled(true);
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(AddClient.this, "Confirm password field cannot be empty", Toast.LENGTH_SHORT).show();
                    submit.setEnabled(true);
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(AddClient.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    submit.setEnabled(true);
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(AddClient.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    submit.setEnabled(true);
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task) -> {
                    if(task.isSuccessful()) {
                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Email sent.");
                                        }
                                    }
                                });

                        userId = mAuth.getCurrentUser().getUid();
                        DocumentReference usersCollection = db.collection("users").document(userId);
                        Map<String, Object> user = new HashMap<>();
                        user.put("first_name", firstName);
                        user.put("last_name", lastName);
                        user.put("email", email);
                        user.put("role", role);
                        user.put("uid", userId);
                        usersCollection.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess: user Profile is created for "+userId);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: "+e.toString());
                            }
                        });
                        DocumentReference clientsCollection = db.collection("clients").document(userId);
                        Map<String, Object> client = new HashMap<>();
                        client.put("first_name", firstName);
                        client.put("last_name", lastName);
                        client.put("email", email);
                        client.put("uid", userId);
                        clientsCollection.set(client).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess: user Profile is created for "+userId);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: "+e.toString());
                            }
                        });
                        Toast.makeText(AddClient.this, "Email sent, please verify your email address", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(AddClient.this, "Email already exists", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
                submit.setEnabled(true);
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
}