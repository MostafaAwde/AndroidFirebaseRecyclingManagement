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

public class ManageMaterials extends AppCompatActivity {

    CollectionReference materialsRef;
    Button addMaterial, back;
    ListView ls;
    ArrayAdapter<Material> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_materials);

        materialsRef = FirebaseFirestore.getInstance().collection("materials");

        addMaterial = findViewById(R.id.btnAddNewMaterial);
        back = findViewById(R.id.backToManagerHome);

        ls = findViewById(R.id.listViewMaterials);

        adapter =  new ArrayAdapter<Material>(this, android.R.layout.simple_list_item_1, new ArrayList<Material>());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Manager_home_page.class);
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
                Log.d(TAG, "adapter size: "+adapter.getCount());

                Log.d(TAG, "Material List Size: " + materialsList.size());
            } else {
                Log.d(TAG, "Error getting materials: ", task.getException());
            }
        });
        ls.setAdapter(adapter);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Material selectedMaterial = adapter.getItem(position);

                // Create an intent to start the DeleteClientActivity
                Intent intent = new Intent(getApplicationContext(), DeleteUpdateMaterial.class);

                // Pass the client data through intent extras
                intent.putExtra("quantity", selectedMaterial.getQuantity());
                intent.putExtra("type", selectedMaterial.getType());
                intent.putExtra("uid", selectedMaterial.getUid());

                startActivity(intent);
                finish();
            }
        });

        addMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMaterial.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(), AddMaterial.class);
                startActivity(intent);
                finish();
                addMaterial.setEnabled(true);
            }
        });

    }
}