package com.example.worktracker_pog_sales;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class pogActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Declare UI elements
    Spinner inventorySpin, typeSpin, monthSpin;
    Button contButton, saveButton, cancelButton, dateButton;

    // Strings to store selected values
    String storeDate = "";
    String storeMonth = "";
    String storeType = "";
    String storeInventory = "";

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pog);

        // Initialize UI elements
        inventorySpin = findViewById(R.id.pog_inventory);
        typeSpin = findViewById(R.id.pog_type);
        monthSpin = findViewById(R.id.pog_month);
        dateButton = findViewById(R.id.pog_date);
        contButton = findViewById(R.id.pog_continue);
        saveButton = findViewById(R.id.pog_save);
        cancelButton = findViewById(R.id.pog_cancel);

        dateButton.setText("");
        initDatePicker();

        /*
          Spinners
         */

        // Spinner for Months
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.Months,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpin.setAdapter(adapter);
        monthSpin.setOnItemSelectedListener(this);

        // Spinner for Inventory
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this,
                R.array.Inventories,
                android.R.layout.simple_spinner_item
        );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inventorySpin.setAdapter(adapter1);
        inventorySpin.setOnItemSelectedListener(this);

        // Spinner for Type
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.Types,
                android.R.layout.simple_spinner_item
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpin.setAdapter(adapter2);
        typeSpin.setOnItemSelectedListener(this);

        // CONTINUE BUTTON
        contButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidInput()) {
                    storeDataToFirebase();
                    Intent intent = new Intent(pogActivity.this, dump.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(pogActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // SAVE BUTTON
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeDataLocally();
            }
        });

        // CANCEL BUTTON
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(pogActivity.this, salesActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method to check if all fields are filled
    private boolean isValidInput() {
        return !storeDate.isEmpty() && !storeMonth.isEmpty() && !storeType.isEmpty() && !storeInventory.isEmpty();
    }

    // Store data to Firebase
    private void storeDataToFirebase() {
        // Initialize Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://scl-filipinas-work-tracker-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("POG");

        // Retrieve selected values from spinners and date button
        storeMonth = monthSpin.getSelectedItem().toString();
        storeInventory = inventorySpin.getSelectedItem().toString();
        storeType = typeSpin.getSelectedItem().toString();

        // Create a data object
        DataClass dataclass = new DataClass(storeDate, storeMonth, storeType, storeInventory);

        // Generate a unique identifier
        String uniqueId = Long.toString(System.currentTimeMillis());

        // Set the value with the key appended with the unique identifier
        myRef.child(storeDate + "_" + uniqueId).setValue(dataclass);

        Toast.makeText(pogActivity.this, "Data saved to database", Toast.LENGTH_SHORT).show();
    }

    // Store data locally
    private void storeDataLocally() {
        // Retrieve selected values from spinners and date button
        storeMonth = monthSpin.getSelectedItem().toString();
        storeInventory = inventorySpin.getSelectedItem().toString();
        storeType = typeSpin.getSelectedItem().toString();

        // Save the data to local variables or preferences
        SharedPreferences preferences = getSharedPreferences("LocalData", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("storedDate", storeDate);
        editor.putString("storedMonth", storeMonth);
        editor.putString("storedInventory", storeInventory);
        editor.putString("storedType", storeType);
        editor.apply();

        Toast.makeText(pogActivity.this, "Progress saved locally", Toast.LENGTH_SHORT).show();
    }

    // Spinner item selected listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Retrieve selected values from spinners and date button
        storeMonth = monthSpin.getSelectedItem().toString();
        storeInventory = inventorySpin.getSelectedItem().toString();
        storeType = typeSpin.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Handle situation where nothing is selected if needed
    }

    /*
      Functions for Date Button
     */
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                storeDate = makeDateString(day, month, year);
                dateButton.setText(storeDate);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    // Format date string
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    // Get month abbreviation
    private String getMonthFormat(int month) {
        String[] monthsArray = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        if (month >= 1 && month <= 12)
            return monthsArray[month - 1];
        else
            return "JAN";
    }
}
