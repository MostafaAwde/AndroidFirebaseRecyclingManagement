package com.example.recycling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Client_home_page extends AppCompatActivity {

    Button signOut, viewRequests, viewMaterials;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home_page);

        viewRequests = findViewById(R.id.btnViewRequests);
        viewMaterials = findViewById(R.id.btnViewMaterials);
        signOut = findViewById(R.id.btnSignOutClient);

        mAuth = FirebaseAuth.getInstance();

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut.setEnabled(false);
                mAuth.signOut();
                signUserOut();
                signOut.setEnabled(true);
            }
        });

        viewMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClientViewMaterials.class);
                startActivity(intent);
                finish();
            }
        });

        viewRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClientRequests.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signUserOut() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }
}