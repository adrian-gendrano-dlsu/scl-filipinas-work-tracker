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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class salesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Declare UI elements
    Spinner customerSpin, productSpin;
    Button contButton, saveButton, cancelButton, dateButton;
    EditText priceText, valueText, unitText;

    // Strings to store selected values
    String storeDate = "";
    String storeProduct = "";
    String storeCustomer = "";

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        // Initialize UI elements
        priceText = findViewById(R.id.sales_price);
        valueText = findViewById(R.id.sales_value);
        unitText = findViewById(R.id.sales_unit);
        productSpin = findViewById(R.id.sales_product);
        customerSpin = findViewById(R.id.sales_customer);
        dateButton = findViewById(R.id.sales_date);
        contButton = findViewById(R.id.sales_continue);
        saveButton = findViewById(R.id.sales_save);
        cancelButton = findViewById(R.id.sales_cancel);

        dateButton.setText("");
        initDatePicker();

        /*
          Spinners
         */

        // Spinner for Customer
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.Customer,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customerSpin.setAdapter(adapter);
        customerSpin.setOnItemSelectedListener(this);

        // Spinner for Product
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this,
                R.array.Products,
                android.R.layout.simple_spinner_item
        );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpin.setAdapter(adapter1);
        productSpin.setOnItemSelectedListener(this);


        // CONTINUE BUTTON
        contButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidInput()) {
                    // Extracting values from UI elements
                    String storeUnit = unitText.getText().toString();
                    String storePrice = priceText.getText().toString();
                    String storeValue = valueText.getText().toString();

                    // Storing data to Firebase
                    storeDataToFirebase(storeUnit, storePrice, storeValue);
                } else {
                    Toast.makeText(salesActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // SAVE BUTTON
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Extracting values from UI elements
                String storeUnit = unitText.getText().toString();
                String storePrice = priceText.getText().toString();
                String storeValue = valueText.getText().toString();

                // Storing data locally
                storeDataLocally(storeUnit, storePrice, storeValue);
            }
        });

        // CANCEL BUTTON
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(salesActivity.this, dump.class);
                startActivity(intent);
            }
        });
    }

    // Method to check if all fields are filled
    private boolean isValidInput() {
        return !storeDate.isEmpty() && !storeCustomer.isEmpty() && !storeProduct.isEmpty();
    }

    private void storeDataToFirebase(String storeUnit, String storePrice, String storeValue) {
        // Initialize Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://scl-filipinas-work-tracker-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Sales");

        // Convert unit, price, and value to appropriate types
        int unit = Integer.parseInt(storeUnit);
        double price = Double.parseDouble(storePrice);
        double value = Double.parseDouble(storeValue);

        // Create a data object
        SalesDataClass salesdataclass = new SalesDataClass(storeDate, storeCustomer, storeProduct, unit, price, value);

        // Generate a unique identifier
        String uniqueId = Long.toString(System.currentTimeMillis());

        // Set the value with the key appended with the unique identifier
        myRef.child(storeDate + "_" + uniqueId).setValue(salesdataclass);

        Toast.makeText(salesActivity.this, "Data saved to database", Toast.LENGTH_SHORT).show();
    }


    // Store data locally
    private void storeDataLocally(String storeUnit, String storePrice, String storeValue) {

        // Save the data to local variables or preferences
        SharedPreferences preferences = getSharedPreferences("LocalData", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("storedDate", storeDate);
        editor.putString("storedProduct", storeProduct);
        editor.putString("storedCustomer", storeCustomer);
        editor.putString("storedUnit", storeUnit);
        editor.putString("storedPrice", storePrice);
        editor.putString("storedValue", storeValue);

        editor.apply();

        Toast.makeText(salesActivity.this, "Progress saved locally", Toast.LENGTH_SHORT).show();
    }

    // Spinner item selected listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Retrieve selected values from spinners and date button
        storeCustomer = customerSpin.getSelectedItem().toString();
        storeProduct = productSpin.getSelectedItem().toString();
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