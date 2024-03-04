package com.example.recycling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Manager_home_page extends AppCompatActivity {

    Button viewClients, viewDrivers, viewMaterials, viewTasks, viewRequests,signOut;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home_page);

        mAuth = FirebaseAuth.getInstance();

        viewClients = findViewById(R.id.btnViewClients);
        viewDrivers = findViewById(R.id.btnViewDrivers);
        viewMaterials = findViewById(R.id.btnViewMaterials);
        viewTasks = findViewById(R.id.btnViewTasks);
        viewRequests = findViewById(R.id.btnViewRequests);
        signOut = findViewById(R.id.btnSignOutManager);

        viewClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewClients.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(), ManageClientsActivity.class);
                startActivity(intent);
                finish();
                viewClients.setEnabled(true);
            }
        });
        viewMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMaterials.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(), ManageMaterials.class);
                startActivity(intent);
                finish();
                viewMaterials.setEnabled(true);
            }
        });
        viewDrivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDrivers.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(), ManageDrivers.class);
                startActivity(intent);
                finish();
                viewDrivers.setEnabled(true);
            }
        });

        viewRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewRequests.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(), ViewRequests.class);
                startActivity(intent);
                finish();
                viewRequests.setEnabled(true);
            }
        });

        viewTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewTasks.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(), ManageTasks.class);
                startActivity(intent);
                finish();
                viewTasks.setEnabled(true);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut.setEnabled(false);
                mAuth.signOut();
                signUserOut();
                signOut.setEnabled(true);
            }
        });
    }

    private void signUserOut() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }
}