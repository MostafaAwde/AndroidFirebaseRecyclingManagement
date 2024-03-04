package com.example.recycling;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textViewForgotPassword, textViewSignup;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && mAuth.getCurrentUser().isEmailVerified()){
            DocumentReference documentReference = db.collection("users").document(currentUser.getUid());
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String role = documentSnapshot.getString("role");
                    if (role != null) {
                        if (role.equals("manager")) {
                            startActivity(new Intent(Login.this, Manager_home_page.class));
                            finish();
                        } else if (role.equals("driver")) {
                            startActivity(new Intent(Login.this, Driver_home_page.class));
                            finish();
                        } else if (role.equals("client")) {
                            startActivity(new Intent(Login.this, Client_home_page.class));
                            finish();
                        }
                    }
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(Login.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.addPassword);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        textViewForgotPassword = findViewById(R.id.forgotPassword);
        textViewSignup = findViewById(R.id.signup);

        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewSignup.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
                finish();
                textViewSignup.setEnabled(true);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLogin.setEnabled(false);
                String email, password;
                email = editTextEmail.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
                    buttonLogin.setEnabled(true);
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(Login.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                    buttonLogin.setEnabled(true);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    buttonLogin.setEnabled(true);
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            DocumentReference documentReference = db.collection("users").document(user.getUid());
                            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String role = documentSnapshot.getString("role");
                                    if (role != null) {
                                        if (role.equals("manager")) {
                                            startActivity(new Intent(Login.this, Manager_home_page.class));
                                            finish();
                                        } else if (role.equals("driver")) {
                                            startActivity(new Intent(Login.this, Driver_home_page.class));
                                            finish();
                                        } else if (role.equals("client")) {
                                            startActivity(new Intent(Login.this, Client_home_page.class));
                                            finish();
                                        }
                                    }
                                }
                            }).addOnFailureListener(e -> {
                                Toast.makeText(Login.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            Toast.makeText(Login.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                    buttonLogin.setEnabled(true);
                });
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewForgotPassword.setEnabled(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
                EditText editTextEmailToReset = dialogView.findViewById(R.id.editTextEmailToReset);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialogView.findViewById(R.id.buttonReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogView.findViewById(R.id.buttonReset).setEnabled(false);
                        String userEmail = editTextEmailToReset.getText().toString().trim();
                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(Login.this, "Enter your registered email", Toast.LENGTH_SHORT).show();
                            dialogView.findViewById(R.id.buttonReset).setEnabled(true);
                            return;
                        }
                        mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(Login.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(Login.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        dialogView.findViewById(R.id.buttonReset).setEnabled(true);
                    }
                });
                dialogView.findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogView.findViewById(R.id.buttonCancel).setEnabled(false);
                        dialog.dismiss();
                        dialogView.findViewById(R.id.buttonCancel).setEnabled(true);
                    }
                });
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
                textViewForgotPassword.setEnabled(true);
            }
        });
    }
}

