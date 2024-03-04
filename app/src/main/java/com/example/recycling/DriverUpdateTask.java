package com.example.recycling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DriverUpdateTask extends AppCompatActivity {

    Button back, updateTask;
    FirebaseFirestore db;
    TextView taskInfo;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_update_task);

        db = FirebaseFirestore.getInstance();
        back = findViewById(R.id.backToViewTasks);
        updateTask = findViewById(R.id.updateStatusToFinished);
        taskInfo = findViewById(R.id.TextViewTaskInfo);
        progressBar = findViewById(R.id.progressBar);

        String city = getIntent().getStringExtra("city");
        String client_first_name = getIntent().getStringExtra("client_first_name");
        String client_last_name = getIntent().getStringExtra("client_last_name");
        String client_id = getIntent().getStringExtra("client_id");
        String material_type = getIntent().getStringExtra("material_type");
        String quantity_requested = getIntent().getStringExtra("quantity_requested");
        String driver_Id = getIntent().getStringExtra("driver_Id");
        String status = getIntent().getStringExtra("status");
        String taskId = getIntent().getStringExtra("taskId");

        taskInfo.setText("City: "+city+"\nClient First Name: "+client_first_name+"\nClient Last Name: "+client_last_name+
                "\nClient Id: "+client_id+"\nTask Id: "+taskId+"\nMaterial Type: "+material_type+"\nQuantity Requested: "+quantity_requested+"\nDriver Id: "+driver_Id+
                "\nStatus: "+status);

        updateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTask.setEnabled(false);
                updateStatus(taskId);
                updateTask.setEnabled(true);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DriverViewTasks.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateStatus(String uid) {
        String status = "finished";

        progressBar.setVisibility(View.VISIBLE);

        DocumentReference materialsRef = db.collection("tasks").document(uid);

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("status", status);

        // Update the document with the new data
        materialsRef.update(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Material updated successfully
                        Toast.makeText(DriverUpdateTask.this, "Task status updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), DriverViewTasks.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update material
                        Toast.makeText(DriverUpdateTask.this, "Failed to update task status", Toast.LENGTH_SHORT).show();
                    }
                });
        progressBar.setVisibility(View.GONE);
    }
}