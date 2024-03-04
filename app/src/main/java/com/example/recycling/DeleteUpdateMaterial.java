package com.example.recycling;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DeleteUpdateMaterial extends AppCompatActivity {

    EditText newQuantity;
    FirebaseFirestore db;
    TextView materialInfo;
    Button delete, update, back;
    ProgressBar progressBar, progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_update_material);

        db = FirebaseFirestore.getInstance();
        newQuantity = findViewById(R.id.editTextNewQuantity);
        delete = findViewById(R.id.deleteSelectedMaterial);
        back = findViewById(R.id.backToManageMaterials);
        update = findViewById(R.id.btnUpdateMaterial);
        materialInfo = findViewById(R.id.TextViewMaterialInfo);
        progressBar = findViewById(R.id.progressBar);
        progressBar1 = findViewById(R.id.progressBar1);

        String materialQuantity = getIntent().getStringExtra("quantity");
        String materialType = getIntent().getStringExtra("type");
        String materialUid = getIntent().getStringExtra("uid");

        materialInfo.setText("Quantity: "+materialQuantity+"\nType: "+materialType+"\nuid: "+materialUid);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.setEnabled(false);
                deleteMaterial(materialUid);
                delete.setEnabled(true);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update.setEnabled(false);
                updateMaterial(materialUid);
                update.setEnabled(true);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManageMaterials.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void updateMaterial(String uid) {
        // Get the updated quantity from the input field
        String quantity = newQuantity.getText().toString().trim();

        // Validate the input
        if (quantity.isEmpty()) {
            newQuantity.setError("Quantity is required");
            newQuantity.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Update the material in Firestore
        DocumentReference materialsRef = db.collection("materials").document(uid);

        // Create a map with the updated data (excluding the uid field)
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("quantity", quantity);

        // Update the document with the new data
        materialsRef.update(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Material updated successfully
                        Toast.makeText(DeleteUpdateMaterial.this, "Material updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ManageMaterials.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update material
                        Toast.makeText(DeleteUpdateMaterial.this, "Failed to update material", Toast.LENGTH_SHORT).show();
                    }
                });
        progressBar.setVisibility(View.GONE);
    }

    private void deleteMaterial(String uid) {
        progressBar1.setVisibility(View.VISIBLE);
        // Get a reference to the Firestore collection
        CollectionReference materialsRef = FirebaseFirestore.getInstance().collection("materials");

        // Delete the material document with the specified UID
        materialsRef.document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        materialsRef.document(uid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DeleteUpdateMaterial.this, "Material has been deleted", Toast.LENGTH_SHORT).show();
                                materialInfo.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Material is not deleted");
                            }
                        });
                    }
                });
        progressBar1.setVisibility(View.GONE);
    }
}