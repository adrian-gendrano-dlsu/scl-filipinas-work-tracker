package com.example.worktracker

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextName: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var editTextContactNumber: EditText
    private lateinit var spinnerTerritory: Spinner
    private lateinit var buttonReg: Button
    private lateinit var progressbar: ProgressBar
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        editTextName = findViewById(R.id.name)
        editTextAddress = findViewById(R.id.address)
        editTextContactNumber = findViewById(R.id.contactNumber)
        spinnerTerritory = findViewById(R.id.territory)
        buttonReg = findViewById(R.id.btn_register)
        progressbar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.   loginNow)
        val database = FirebaseDatabase.getInstance()
        val reference: DatabaseReference = database.reference

        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(
            this,
            R.array.Territories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinnerTerritory.onItemSelectedListener = this
            spinnerTerritory.adapter = adapter
        }

        textView.setOnClickListener{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }

        buttonReg.setOnClickListener {
            progressbar.visibility = View.VISIBLE
            val email: String = editTextEmail.text.toString().trim();
            val password: String = editTextPassword.text.toString().trim();
            val name: String = editTextName.text.toString().trim();
            val address: String = editTextAddress.text.toString().trim();
            val contactNumber: String = editTextContactNumber.text.toString().trim();
            val territory: String = spinnerTerritory.selectedItem.toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
                progressbar.visibility = View.GONE
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
                progressbar.visibility = View.GONE
                return@setOnClickListener
            }

            if (name.isEmpty() or address.isEmpty() or contactNumber.isEmpty()) {
                Toast.makeText(this, "Incomplete Information", Toast.LENGTH_SHORT).show()
                progressbar.visibility = View.GONE
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressbar.visibility = View.GONE
                    if (task.isSuccessful) {
                        val user = User(name, address, contactNumber, territory)
                        auth.getCurrentUser()
                            ?.let { it1 -> reference.child("users").child(it1.uid).setValue(user) };
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(baseContext, "Account Created", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        try {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            throw task.exception!!;
                        }
                        catch(e: FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(baseContext, "Invalid Email Format", Toast.LENGTH_SHORT)
                                .show()
                        }
                        catch(e: FirebaseAuthUserCollisionException) {
                            Toast.makeText(baseContext, "Email already taken", Toast.LENGTH_SHORT)
                                .show()
                        }
                        catch(e: Exception) {
                            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedItem = parent?.getItemAtPosition(position).toString()
        Toast.makeText(this, "Selected Item: $selectedItem", Toast.LENGTH_SHORT).show()

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, "Nothing selected", Toast.LENGTH_SHORT).show()
    }
}