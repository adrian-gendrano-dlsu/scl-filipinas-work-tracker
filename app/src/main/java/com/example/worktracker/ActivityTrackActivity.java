package com.example.worktracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityTrackActivity extends AppCompatActivity {
    private TextView status;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Just for appearance
        setContentView(R.layout.activity_activitytrack);

        Button goBackBtn = findViewById(R.id.goBackBtn);
        Button saveBtn = findViewById(R.id.saveBtn);
        EditText dateField = findViewById(R.id.dateField);
        EditText activityTypeField = findViewById(R.id.activityTypeField);
        EditText targetCropsField = findViewById(R.id.targetCropsField);
        EditText attendanceField = findViewById(R.id.attendanceField);
        EditText activityCostField = findViewById(R.id.activityCostField);
        status = findViewById(R.id.status);

        // Back button
        goBackBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Save button
        saveBtn.setOnClickListener(v -> {
            // TODO: Append to .csv file

            // Concatenate input values
            status.setText(getString(R.string.saved));
        });

        // Just for appearance
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
