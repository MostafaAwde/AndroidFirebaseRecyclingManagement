package com.example.recycling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ClientRequests extends AppCompatActivity {

    CollectionReference requestsRef;
    Button addRequest, back;
    ListView listView;
    ArrayAdapter<Request> adapter;
    String clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_requests);

        requestsRef = FirebaseFirestore.getInstance().collection("requests");
        addRequest = findViewById(R.id.btnAddNewRequest);
        back = findViewById(R.id.backToClientHome);

        listView = findViewById(R.id.listViewClientRequests);
        clientId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        adapter =  new ArrayAdapter<Request>(this, android.R.layout.simple_list_item_1, new ArrayList<Request>());
        listView.setAdapter(adapter);

        addRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientRequests.this, ClientSendRequestActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request selectedRequest = adapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), ClientDeleteRequest.class);

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Client_home_page.class);
                startActivity(intent);
                finish();
            }
        });

        // Retrieve and display the client's requests
        requestsRef.whereEqualTo("client_id", clientId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Request> requests = queryDocumentSnapshots.toObjects(Request.class);
                    adapter.addAll(requests);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error getting client's requests", Toast.LENGTH_SHORT).show();
                });

    }
}