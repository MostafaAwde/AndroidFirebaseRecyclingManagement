package com.example.recycling;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ClientViewMaterials extends AppCompatActivity {

    CollectionReference materialsRef;

    Button back;

    ListView ls;
    ArrayAdapter<Material> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_view_materials);

        materialsRef = FirebaseFirestore.getInstance().collection("materials");

        back = findViewById(R.id.backToClientHome);

        ls = findViewById(R.id.listViewMaterails);

        adapter =  new ArrayAdapter<Material>(this, android.R.layout.simple_list_item_1, new ArrayList<Material>());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Client_home_page.class);
                startActivity(intent);
                finish();
            }
        });

        materialsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Material> materialsList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Material material = document.toObject(Material.class);
                    materialsList.add(material);
                }
                adapter.clear();
                adapter.addAll(materialsList);
            } else {
                Log.d(TAG, "Error getting materials: ", task.getException());
            }
        });
        ls.setAdapter(adapter);
    }
}