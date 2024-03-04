package com.example.recycling;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageClientsActivity extends AppCompatActivity {

    CollectionReference clientsRef;
    Button addClient, back;
    ListView ls;
    ArrayAdapter<Client> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_clients);

        clientsRef = FirebaseFirestore.getInstance().collection("clients");

        addClient = findViewById(R.id.btnAddNewClient);
        back = findViewById(R.id.backToManagerHome);

        ls = findViewById(R.id.listViewClients);

        adapter =  new ArrayAdapter<Client>(this, android.R.layout.simple_list_item_1, new ArrayList<Client>());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Manager_home_page.class);
                startActivity(intent);
                finish();
            }
        });
        clientsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Client> clientList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Client client = document.toObject(Client.class);
                    clientList.add(client);
                }
                adapter.clear();
                adapter.addAll(clientList);
                Log.d(TAG, "adapter size: "+adapter.getCount());

                Log.d(TAG, "Client List Size: " + clientList.size());
                for (Client client : clientList) {
                    Log.d(TAG, "Client: " + client.getFirst_name() + " " + client.getLast_name() + " - ID: " + client.getUid());
                }
            } else {
                Log.d(TAG, "Error getting clients: ", task.getException());
            }
        });
        ls.setAdapter(adapter);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Client selectedClient = adapter.getItem(position);

                // Create an intent to start the DeleteClientActivity
                Intent intent = new Intent(getApplicationContext(), DeleteClientActivity.class);

                // Pass the client data through intent extras
                intent.putExtra("first_name", selectedClient.getFirst_name());
                intent.putExtra("last_name", selectedClient.getLast_name());
                intent.putExtra("uid", selectedClient.getUid());
                intent.putExtra("email", selectedClient.getEmail());

                startActivity(intent);
                finish();
            }
        });

        addClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClient.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(), AddClient.class);
                startActivity(intent);
                finish();
                addClient.setEnabled(true);
            }
        });
    }
}