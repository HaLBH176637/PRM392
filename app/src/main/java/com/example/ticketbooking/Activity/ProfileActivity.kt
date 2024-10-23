package com.example.ticketbooking.Activity

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ticketbooking.R

class ProfileActivity : AppCompatActivity() {

    private lateinit var fullnameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var dateOfBirthTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        fullnameTextView = findViewById(R.id.textViewFullName)
        emailTextView = findViewById(R.id.textViewEmail)
        addressTextView = findViewById(R.id.editTextAddress)
        dateOfBirthTextView = findViewById(R.id.editTextDateOfBirth)
        phoneTextView = findViewById(R.id.textViewPhone)
        genderTextView = findViewById(R.id.editTextGender)

        // Get SharedPreferences
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        // Load user data from SharedPreferences
        loadUserData()
    }

    private fun loadUserData() {
        val fullName = sharedPreferences.getString("userFullName", "N/A")
        val email = sharedPreferences.getString("userEmail", "N/A")
        val address = sharedPreferences.getString("userAddress", "N/A")
        val dateOfBirth = sharedPreferences.getString("userDateOfBirth", "N/A")
        val phone = sharedPreferences.getString("userPhone", "N/A")
        val gender = sharedPreferences.getString("userGender", "N/A")

        // Set the data to TextViews
        fullnameTextView.text = fullName
        emailTextView.text = email
        addressTextView.text = address
        dateOfBirthTextView.text = dateOfBirth
        phoneTextView.text = phone
        genderTextView.text = gender
    }
}
