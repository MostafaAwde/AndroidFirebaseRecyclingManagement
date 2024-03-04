package com.example.recycling;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ManageTasks extends AppCompatActivity {

    CollectionReference tasksRef;
    ListView ls;
    Button back;
    ArrayAdapter<Task> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tasks);

        tasksRef = FirebaseFirestore.getInstance().collection("tasks");
        back = findViewById(R.id.backToManagerHome);

        ls = findViewById(R.id.listViewTasks);

        adapter =  new ArrayAdapter<Task>(this, android.R.layout.simple_list_item_1, new ArrayList<Task>());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Manager_home_page.class);
                startActivity(intent);
                finish();
            }
        });

        tasksRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Task> tasksList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Task tsk = document.toObject(Task.class);
                    tasksList.add(tsk);
                }
                adapter.clear();
                adapter.addAll(tasksList);
                Log.d(TAG, "adapter size: "+adapter.getCount());

                Log.d(TAG, "Task List Size: " + tasksList.size());
            } else {
                Log.d(TAG, "Error getting tasks: ", task.getException());
            }
        });
        ls.setAdapter(adapter);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task selectedTask = adapter.getItem(position);

                // Create an intent to start the DeleteClientActivity
                Intent intent = new Intent(getApplicationContext(), DeleteTask.class);

                // Pass the client data through intent extras
                intent.putExtra("city", selectedTask.getCity());
                intent.putExtra("client_first_name", selectedTask.getClient_first_name());
                intent.putExtra("client_last_name", selectedTask.getClient_last_name());
                intent.putExtra("client_id", selectedTask.getClient_id());
                intent.putExtra("material_type", selectedTask.getMaterial_type());
                intent.putExtra("quantity_requested", selectedTask.getQuantity_requested());
                intent.putExtra("driver_Id", selectedTask.getDriver_Id());
                intent.putExtra("status", selectedTask.getStatus());
                intent.putExtra("taskId", selectedTask.getTaskId());

                startActivity(intent);
                finish();
            }
        });
    }
}