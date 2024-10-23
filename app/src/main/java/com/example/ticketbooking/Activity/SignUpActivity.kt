package com.example.ticketbooking.Activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ticketbooking.Models.User // Đảm bảo import đúng lớp User từ package Models
import com.example.ticketbooking.databinding.ActivitySignupBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo Firebase Realtime Database
        database = FirebaseDatabase.getInstance().getReference("User")

        binding.signupButton.setOnClickListener {
            val fullName = binding.fullNameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val dob = binding.dobEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()
            val address = binding.addressEditText.text.toString().trim()
            val selectedGenderId = binding.gender.checkedRadioButtonId
            val gender = findViewById<RadioButton>(selectedGenderId)?.text.toString()

            // Kiểm tra các trường nhập liệu
            if (TextUtils.isEmpty(fullName)) {
                binding.fullNameEditText.error = "Full Name is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(email)) {
                binding.emailEditText.error = "Email is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                binding.passwordEditText.error = "Password is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(dob)) {
                binding.dobEditText.error = "Date of Birth is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(phone)) {
                binding.phoneEditText.error = "Phone Number is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(address)) {
                binding.addressEditText.error = "Address is required"
                return@setOnClickListener
            }

            // Tạo ID người dùng ngẫu nhiên trong database
            val id = database.push().key

            // Tạo đối tượng người dùng với role mặc định là "user"
            val user = User(
                id = id ?: "",
                fullname = fullName,
                email = email,
                phone = phone,
                dateOfBirth = dob,
                gender = gender,
                address = address,
                password = password,
                role = "user" // Role mặc định là "user"
            )

            // Lưu người dùng vào Realtime Database
            if (id != null) {
                database.child(id).setValue(user).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()

                        // Điều hướng đến màn hình đăng nhập sau khi đăng ký thành công
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Sign up failed: " + task.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        // Chuyển hướng đến màn hình đăng nhập nếu đã có tài khoản
        binding.loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
