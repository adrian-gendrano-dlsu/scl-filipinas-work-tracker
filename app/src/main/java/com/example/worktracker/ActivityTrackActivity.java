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

import java.util.ArrayList;

public class ActivityTrackActivity extends AppCompatActivity {
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitytrack);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://scl-filipinas-work-tracker-default-rtdb.asia-southeast1.firebasedatabase.app/a"); // Replace with your database URL
        DatabaseReference activitiesRef = database.getReference("Activities");
        DatabaseReference activityTypeRef = database.getReference("ActivityTypes");
        DatabaseReference cropRef = database.getReference("Crops");

        // UI Elements
        Button goBackBtn = findViewById(R.id.goBackBtn);
        Button saveBtn = findViewById(R.id.saveBtn);
        EditText dateField = findViewById(R.id.dateField);
        Spinner activityTypeField = findViewById(R.id.activityTypeField);
        Spinner targetCropsField = findViewById(R.id.targetCropsField);
        EditText attendanceField = findViewById(R.id.attendanceField);
        EditText activityCostField = findViewById(R.id.activityCostField);
        status = findViewById(R.id.status);

        // Fetch and display data
        fetchAndDisplayData(activityTypeRef, activityTypeField);
        fetchAndDisplayData(cropRef, targetCropsField);

        // Back button
        goBackBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Save button
        saveBtn.setOnClickListener(v -> {
            String date = dateField.getText().toString();
            String activityType = activityTypeField.getSelectedItem().toString();
            String targetCrop = targetCropsField.getSelectedItem().toString();
            String attendance = attendanceField.getText().toString();
            String activityCost = activityCostField.getText().toString();

            if (date.isEmpty() || activityType.isEmpty() || targetCrop.isEmpty() || attendance.isEmpty() || activityCost.isEmpty()) {
                status.setText(getString(R.string.empty));
            } else {
                activitiesRef.push().setValue(date + "," + activityType + "," + targetCrop + "," + attendance + "," + activityCost);
                status.setText(getString(R.string.saved));
            }
        });
    }

    private void fetchAndDisplayData(DatabaseReference ref, Spinner spinner) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> values = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    values.add(snapshot.getValue(String.class)); // Assumes the data is of type String
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ActivityTrackActivity.this,
                        android.R.layout.simple_list_item_1, values);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                status.setText("Failed to load data");
            }
        });
    }
}
