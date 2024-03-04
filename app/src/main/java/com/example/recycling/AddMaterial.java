package com.example.recycling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class AddMaterial extends AppCompatActivity {

    TextInputEditText editQuantity, editType;

    FirebaseFirestore db;

    Button submit, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material);

        db = FirebaseFirestore.getInstance();

        submit = findViewById(R.id.btnSubmitMaterial);
        back = findViewById(R.id.btnBackToManageMaterials);
        editQuantity = findViewById(R.id.quantity);
        editType = findViewById(R.id.type);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setEnabled(false);
                addMaterial();
                submit.setEnabled(true);
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

    private void addMaterial() {
        // Get the input values
        String quantity = editQuantity.getText().toString().trim();
        String type = editType.getText().toString().trim();

        // Validate the input
        if (quantity.isEmpty()) {
            editQuantity.setError("Quantity is required");
            editQuantity.requestFocus();
            return;
        }
        if (type.isEmpty()) {
            editType.setError("Type is required");
            editType.requestFocus();
            return;
        }
        // Generate a unique uid for the material
        String uid = UUID.randomUUID().toString();

        // Create a new Material object
        Material material = new Material(quantity, type, uid);

        // Add the material to Firestore
        db.collection("materials")
                .document(uid)
                .set(material)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Material added successfully
                        Toast.makeText(AddMaterial.this, "Material added successfully", Toast.LENGTH_SHORT).show();
                        // Reset the input fields
                        editQuantity.setText("");
                        editType.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add material
                        Toast.makeText(AddMaterial.this, "Failed to add material", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}