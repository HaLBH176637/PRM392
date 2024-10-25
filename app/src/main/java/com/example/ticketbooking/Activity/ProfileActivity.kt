package com.example.ticketbooking.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ticketbooking.R
import com.example.ticketbooking.databinding.ActivityProfileBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var fullnameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var dateOfBirthTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var logoutButton: Button
    private lateinit var editButton: Button
    private lateinit var database: DatabaseReference
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var radioMale: RadioButton
    private lateinit var radioFemale: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("User")

        // Initialize views
        radioGroupGender = findViewById(R.id.radioGroupGender)
        radioMale = findViewById(R.id.radioMale)
        radioFemale = findViewById(R.id.radioFemale)
        fullnameTextView = findViewById(R.id.textViewFullName)
        emailTextView = findViewById(R.id.textViewEmail)
        addressTextView = findViewById(R.id.editTextAddress)
        dateOfBirthTextView = findViewById(R.id.editTextDateOfBirth)
        phoneTextView = findViewById(R.id.textViewPhone)
        editButton = findViewById(R.id.buttonEdit)
        logoutButton = findViewById(R.id.buttonLogout)

        // Get SharedPreferences
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        // Load user data from SharedPreferences
        loadUserData()

        // Update user profile
        editButton.setOnClickListener {
            saveUserDataToFirebase()
        }

        // Handle logout
        logoutButton.setOnClickListener {
            logoutUser()
        }

        // Navigation bar logic
        val bottomNav = findViewById<ChipNavigationBar>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { itemId ->
            when (itemId) {
                R.id.menu -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                // Handle other menu items
                R.id.favorites -> { /* Add logic for favorites if needed */ }
                R.id.cart -> { /* Add logic for cart if needed */ }
            }
        }
    }

    private fun loadUserData() {
        val fullName = sharedPreferences.getString("userFullName", "N/A")
        val email = sharedPreferences.getString("userEmail", "N/A")
        val address = sharedPreferences.getString("userAddress", "N/A")
        val dateOfBirth = sharedPreferences.getString("userDateOfBirth", "N/A")
        val phone = sharedPreferences.getString("userPhone", "N/A")
        val gender = sharedPreferences.getString("userGender", "N/A")

        // Set data to TextViews
        fullnameTextView.text = fullName
        emailTextView.text = email
        addressTextView.text = address
        dateOfBirthTextView.text = dateOfBirth
        phoneTextView.text = phone

        // Set gender based on value from SharedPreferences
        if (gender == "Nam") {
            radioMale.isChecked = true
        } else if (gender == "Nữ") {
            radioFemale.isChecked = true
        }
    }

    private fun saveUserDataToFirebase() {
        val fullName = fullnameTextView.text.toString()
        val email = emailTextView.text.toString()
        val address = addressTextView.text.toString()
        val dateOfBirth = dateOfBirthTextView.text.toString()
        val phone = phoneTextView.text.toString()
        val gender = if (radioMale.isChecked) "Nam" else "Nữ"

        // Lấy userId từ SharedPreferences (hoặc từ bất kỳ nơi nào bạn đang lưu userId)
        val userId = sharedPreferences.getString("userId", null) ?: return
        Log.d("ProfileActivity", "userId: $userId") // Ghi lại userId

        // Tạo map chứa thông tin user cần update
        val userMap = mapOf(
            "fullname" to fullName,
            "email" to email,
            "address" to address,
            "dateOfBirth" to dateOfBirth,
            "phone" to phone,
            "gender" to gender
        )

        // Tìm user có id = userId và cập nhật dữ liệu của họ
        val query = database.orderByChild("id").equalTo(userId)
        query.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                // Nếu tìm thấy user, tiến hành cập nhật
                for (childSnapshot in dataSnapshot.children) {
                    // Cập nhật vào user với id tương ứng
                    childSnapshot.ref.updateChildren(userMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            saveUserToSharedPreferences(fullName, email, address, dateOfBirth, phone, gender)
                            Toast.makeText(this, "Cập nhật hồ sơ thành công", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Không thể cập nhật hồ sơ", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Không tìm thấy user với id: $userId", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Log.e("ProfileActivity", "Lỗi khi tìm user: ${exception.message}")
            Toast.makeText(this, "Lỗi khi tìm user: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }



    private fun saveUserToSharedPreferences(fullName: String, email: String, address: String, dateOfBirth: String, phone: String, gender: String) {
        val editor = sharedPreferences.edit()
        editor.putString("userFullName", fullName)
        editor.putString("userEmail", email)
        editor.putString("userAddress", address)
        editor.putString("userDateOfBirth", dateOfBirth)
        editor.putString("userPhone", phone)
        editor.putString("userGender", gender)
        editor.apply()
    }

    private fun logoutUser() {
        // Clear user data from SharedPreferences
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // Redirect to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
