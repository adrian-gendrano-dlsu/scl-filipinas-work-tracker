package com.example.worktracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityTrackActivity : AppCompatActivity() {
    private lateinit var goBackBtn: Button
    private lateinit var saveBtn: Button
    private lateinit var dateField: EditText
    private lateinit var activityTypeField: EditText
    private lateinit var targetCropsField: EditText
    private lateinit var attendanceField: EditText
    private lateinit var activityCostField: EditText
    private lateinit var status: TextView

    public override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // just for appearance
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_activitytrack)

        goBackBtn = findViewById(R.id.goBackBtn)
        saveBtn = findViewById(R.id.saveBtn)
        dateField = findViewById(R.id.dateField)
        activityTypeField = findViewById(R.id.activityTypeField)
        targetCropsField = findViewById(R.id.targetCropsField)
        attendanceField = findViewById(R.id.attendanceField)
        activityCostField = findViewById(R.id.activityCostField)
        status = findViewById(R.id.status)

        // back button
        goBackBtn.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // save button
        saveBtn.setOnClickListener {
            //TODO: Append to .csv file

            // concatenate input values
            val outputStr = "${dateField.text}," +
                    "${activityTypeField.text}," +
                    "${targetCropsField.text}," +
                    "${attendanceField.text}," +
                    "${activityCostField.text}"
            status.text = getString(R.string.saved)
        }

        // just for appearance
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}