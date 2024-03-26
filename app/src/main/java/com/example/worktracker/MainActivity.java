package com.example.worktracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    // Comment to make login-free
//    FirebaseAuth auth;
//    FirebaseUser user;

    Button logoutBtn;
    Button productListBtn;
    Button activityTrackerBtn;
    TextView textViewEmail;
    TextView textViewName;
    List<String> data = new ArrayList<String>();
    String name;
    String address;
    String contactNumber;
    String territory;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Comment to make login-free
//        auth = FirebaseAuth.getInstance();
//        user = auth.getCurrentUser();

        logoutBtn = findViewById(R.id.logout);
        productListBtn = findViewById(R.id.productListBtn);
        activityTrackerBtn = findViewById(R.id.activityTrackerBtn);
        textViewEmail = findViewById(R.id.userEmail);
        textViewName = findViewById(R.id.userName);

        // Comment to make login-free
//        if (user == null) {
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            textViewEmail.setText(user.getEmail());
//            //FirebaseDatabase.getInstance().getReference();
//       }

        productListBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PLPageActivity.class);
            startActivity(intent);
            finish();
        });

        activityTrackerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ActivityTrackActivity.class);
            startActivity(intent);
            finish();
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
