package com.example.worktracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class PLPageActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);

        ListView plListView = findViewById(R.id.plListView);
        Button goBackBtn = findViewById(R.id.goBackBtn);

        // Example data
        String[] productList = {"Product 1", "Product 2", "Product 3", "Product 4"};

        // ArrayAdapter feeds data to plListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        plListView.setAdapter(adapter);

        // Back button
        goBackBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
