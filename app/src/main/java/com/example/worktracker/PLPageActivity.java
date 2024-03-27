package com.example.worktracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PLPageActivity extends AppCompatActivity {

    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);

        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://scl-filipinas-work-tracker-default-rtdb.asia-southeast1.firebasedatabase.app/"); //TODO: Replace
        DatabaseReference productsRef = database.getReference("Products");

        // Initialize UI components
        ListView plListView = findViewById(R.id.plListView);
        Button goBackBtn = findViewById(R.id.goBackBtn);
        status = findViewById(R.id.status);

        // Fetch data from Firebase and display in ListView
        fetchAndDisplayData(productsRef, plListView);

        // Set the goBackBtn button's click listener to go back to MainActivity
        goBackBtn.setOnClickListener(v -> {
            Intent intent = new Intent(PLPageActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void fetchAndDisplayData(DatabaseReference ref, ListView listView) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> values = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming the data is of type String
                    values.add(snapshot.getValue(String.class));
                }
                // Update ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(PLPageActivity.this,
                        android.R.layout.simple_list_item_1, values);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                status.setText("Failed to load data");
            }
        });
    }
}
