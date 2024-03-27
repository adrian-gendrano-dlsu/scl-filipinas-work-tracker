package com.example.worktracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityTrackActivity extends AppCompatActivity {
    private TextView status;
    private Button goBackBtn, pushToDbButton, saveBtn;
    private EditText dateField, attendanceField, productSalesField, activityCostField;
    private Spinner activityTypeField, targetCropsField;
    private DatabaseReference activitiesRef, activityTypeRef, cropRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitytrack);

        initFirebase();
        initViewComponents();
        loadDataFromFile();
        fetchAndDisplayData(activityTypeRef, activityTypeField);
        fetchAndDisplayData(cropRef, targetCropsField);
        setupListeners();
    }

    private void initFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://scl-filipinas-work-tracker-default-rtdb.asia-southeast1.firebasedatabase.app");
        activitiesRef = database.getReference("Activities");
        activityTypeRef = database.getReference("ActivityTypes");
        cropRef = database.getReference("CropList/Vegetables");
    }

    private void initViewComponents() {
        goBackBtn = findViewById(R.id.goBackBtn);
        pushToDbButton = findViewById(R.id.pushToDbButton);
        saveBtn = findViewById(R.id.saveBtn);
        dateField = findViewById(R.id.dateField);
        activityTypeField = findViewById(R.id.activityTypeField);
        targetCropsField = findViewById(R.id.targetCropsField);
        attendanceField = findViewById(R.id.attendanceField);
        productSalesField = findViewById(R.id.productSalesField);
        activityCostField = findViewById(R.id.activityCostField);
        status = findViewById(R.id.status);
    }

    private void loadDataFromFile() {
        File file = new File(getFilesDir(), "activity_list_save.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String date = reader.readLine();
                reader.readLine(); // Skip activity type
                reader.readLine(); // Skip target crop
                String attendance = reader.readLine();
                String productSales = reader.readLine();
                String activityCost = reader.readLine();
                runOnUiThread(() -> {
                    dateField.setText(date);
                    attendanceField.setText(attendance);
                    productSalesField.setText(productSales);
                    activityCostField.setText(activityCost);
                });
            } catch (IOException e) {
                status.setText("Failed to load saved data.");
            }
        }
    }

    private void setupListeners() {
        saveBtn.setOnClickListener(v -> saveDataToFile());
        goBackBtn.setOnClickListener(v -> goBack());
        pushToDbButton.setOnClickListener(v -> pushDataToDb());
    }

    private void saveDataToFile() {
        String filename = "activity_list_save.txt";
        try (FileWriter writer = new FileWriter(new File(getFilesDir(), filename), false)) {
            writer.write(getDataAsString());
            status.setText("Data saved locally");
        } catch (IOException e) {
            status.setText("Failed to save data");
        }
    }

    private void goBack() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void pushDataToDb() {
        // Corrected method to push data into Firebase database
        String date = dateField.getText().toString();
        String targetCrop = targetCropsField.getSelectedItem().toString();
        String attendance = attendanceField.getText().toString();
        String productSales = productSalesField.getText().toString();
        String activityCost = activityCostField.getText().toString();

        if (date.isEmpty() || targetCrop.isEmpty() || attendance.isEmpty() || productSales.isEmpty() || activityCost.isEmpty()) {
            status.setText(getString(R.string.empty));
            return;
        }

        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put("date", date);
        dataToSave.put("targetCrop", targetCrop);
        dataToSave.put("attendance", attendance);
        dataToSave.put("productSales", productSales);
        dataToSave.put("activityCost", activityCost);

        activitiesRef.push().setValue(dataToSave)
                .addOnSuccessListener(aVoid -> status.setText("Pushed to database"))
                .addOnFailureListener(e -> status.setText("Failed to push data to database"));
    }

    private String getDataAsString() {
        return dateField.getText().toString() + "\n" +
                activityTypeField.getSelectedItem().toString() + "\n" +
                targetCropsField.getSelectedItem().toString() + "\n" +
                attendanceField.getText().toString() + "\n" +
                productSalesField.getText().toString() + "\n" +
                activityCostField.getText().toString();
    }

    private void fetchAndDisplayData(DatabaseReference ref, Spinner spinner) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> values = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    values.add(snapshot.getValue(String.class));
                }
                System.out.println(values);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ActivityTrackActivity.this,
                        android.R.layout.simple_spinner_item, values);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                status.setText("Failed to load data from database");
            }
        });
    }
}