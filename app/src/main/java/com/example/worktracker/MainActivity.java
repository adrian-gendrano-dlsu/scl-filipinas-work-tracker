package com.example.worktracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    FirebaseAuth auth;
    Button button;
    TextView textViewEmail;
    TextView textViewName;
    FirebaseUser user;
    List<String> data = new ArrayList<String>();
    String name;
    String address;
    String contactNumber;
    String territory;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textViewEmail = findViewById(R.id.userEmail);
        textViewName = findViewById(R.id.userName);
        user = auth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            textViewEmail.setText(user.getEmail());
            //FirebaseDatabase.getInstance().getReference();
        }
        button.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
