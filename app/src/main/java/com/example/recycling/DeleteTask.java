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

public class DeleteTask extends AppCompatActivity {

    FirebaseFirestore db;
    TextView taskInfo;
    Button delete, back;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_task);

        db = FirebaseFirestore.getInstance();
        delete = findViewById(R.id.deleteSelectedTask);
        back = findViewById(R.id.backToManageTasks);
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.setEnabled(false);
                deleteTask(taskId);
                delete.setEnabled(true);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManageTasks.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void deleteTask(String uid) {
        progressBar.setVisibility(View.VISIBLE);
        // Get a reference to the Firestore collection
        CollectionReference tasksRef = FirebaseFirestore.getInstance().collection("tasks");

        // Delete the material document with the specified UID
        tasksRef.document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        tasksRef.document(uid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DeleteTask.this, "Task has been deleted", Toast.LENGTH_SHORT).show();
                                taskInfo.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Task is not deleted");
                            }
                        });
                    }
                });
        progressBar.setVisibility(View.GONE);
    }
}