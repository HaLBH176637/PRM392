package com.example.ticketbooking.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ticketbooking.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpTextView: TextView
    private lateinit var database: DatabaseReference
    private lateinit var forgetpasswordTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        database = FirebaseDatabase.getInstance().reference.child("User")

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        signUpTextView = findViewById(R.id.signUpTextView)
        forgetpasswordTextView = findViewById(R.id.forgetpasswordTextView)
        loginButton.setOnClickListener { loginUser() }
        signUpTextView.setOnClickListener { openSignUpActivity() }
        forgetpasswordTextView.setOnClickListener { openForgotPasswordActivity() }
    }

    private fun loginUser() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }

        database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val userPassword = userSnapshot.child("password").getValue(String::class.java)
                        val userId = userSnapshot.child("id").getValue(String::class.java)
                        val fullName = userSnapshot.child("fullname").getValue(String::class.java)
                        val address = userSnapshot.child("address").getValue(String::class.java)
                        val dateOfBirth = userSnapshot.child("dateOfBirth").getValue(String::class.java)
                        val phone = userSnapshot.child("phone").getValue(String::class.java)
                        val gender = userSnapshot.child("gender").getValue(String::class.java)
                        val role = userSnapshot.child("role").getValue(String::class.java)

                        if (userPassword == password) {
                            Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()

                            // Lưu thông tin người dùng vào SharedPreferences
                            val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("userId", userId)
                            editor.putString("userEmail", email)
                            editor.putString("userFullName", fullName)
                            editor.putString("userAddress", address)
                            editor.putString("userDateOfBirth", dateOfBirth)
                            editor.putString("userPhone", phone)
                            editor.putString("userGender", gender)
                            editor.putString("userRole", role)
                            editor.apply()

                            // Chuyển đến MainActivity
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Kết thúc Activity đăng nhập
                        } else {
                            Toast.makeText(this@LoginActivity, "Incorrect password", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openSignUpActivity() {
        // Mở Activity đăng ký
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
    private fun openForgotPasswordActivity() {
        // Mở Activity đăng ký
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }
}
