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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ViewRequests extends AppCompatActivity {

    CollectionReference requestsRef;

    Button back;

    ListView ls;
    ArrayAdapter<Request> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        requestsRef = FirebaseFirestore.getInstance().collection("requests");

        back = findViewById(R.id.backToManagerHome);

        ls = findViewById(R.id.listViewRequests);

        adapter =  new ArrayAdapter<Request>(this, android.R.layout.simple_list_item_1, new ArrayList<Request>());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Manager_home_page.class);
                startActivity(intent);
                finish();
            }
        });

        requestsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Request> requestsList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Request request = document.toObject(Request.class);
                    requestsList.add(request);
                }
                adapter.clear();
                adapter.addAll(requestsList);
            } else {
                Log.d(TAG, "Error getting requests: ", task.getException());
            }
        });
        ls.setAdapter(adapter);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request selectedRequest = adapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), ManageRequests.class);

                intent.putExtra("city", selectedRequest.getCity());
                intent.putExtra("client_first_name", selectedRequest.getClient_first_name());
                intent.putExtra("client_id", selectedRequest.getClient_id());
                intent.putExtra("client_last_name",selectedRequest.getClient_last_name());
                intent.putExtra("material_type",selectedRequest.getMaterial_type());
                intent.putExtra("quantity_requested",selectedRequest.getQuantity_requested());
                intent.putExtra("request_id",selectedRequest.getRequest_id());
                startActivity(intent);
                finish();
            }
        });
    }
}