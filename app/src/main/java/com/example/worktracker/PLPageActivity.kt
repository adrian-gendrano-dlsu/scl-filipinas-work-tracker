package com.example.worktracker

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PLPageActivity : AppCompatActivity() {
    private lateinit var goBackBtn: Button
    private lateinit var plListView: ListView

    public override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_productlist)

        plListView = findViewById(R.id.plListView)
        goBackBtn = findViewById(R.id.goBackBtn)

        // Example data
        val productList = arrayOf("Product 1", "Product 2", "Product 3", "Product 4")

        // ArrayAdapter feeds data to plListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, productList)
        plListView.adapter = adapter

        goBackBtn.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}