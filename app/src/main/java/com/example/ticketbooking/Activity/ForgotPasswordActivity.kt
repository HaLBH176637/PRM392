package com.example.ticketbooking.Activity

import com.example.ticketbooking.R
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class ForgotPasswordActivity : AppCompatActivity() {
    //Declaration
    private lateinit var btnReset: Button
    private lateinit var btnBack: Button
    private lateinit var edtEmail: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var mAuth: FirebaseAuth
    private var strEmail: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Khởi tạo các thành phần giao diện
        btnBack = findViewById(R.id.btnForgotPasswordBack)
        btnReset = findViewById(R.id.btnReset)
        edtEmail = findViewById(R.id.edtForgotPasswordEmail)
        progressBar = findViewById(R.id.forgetPasswordProgressbar)

        mAuth = FirebaseAuth.getInstance()

        // Xử lý sự kiện khi nhấn nút Reset
        btnReset.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            if (!TextUtils.isEmpty(email)) {
                strEmail = email
                resetPassword()
            } else {
                edtEmail.error = "Email field can't be empty"
            }
        }

        // Xử lý sự kiện khi nhấn nút Back
        btnBack.setOnClickListener { onBackPressed() }
    }

    private fun resetPassword() {
        val email = edtEmail.text.toString().trim()

        if (email.isNotEmpty()) {
            progressBar.visibility = View.VISIBLE
            btnReset.visibility = View.INVISIBLE

            mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    progressBar.visibility = View.INVISIBLE
                    btnReset.visibility = View.VISIBLE

                    if (task.isSuccessful) {
                        Toast.makeText(this, "Reset password link sent to your email", Toast.LENGTH_SHORT).show()
                    } else {
                        // Hiển thị thông báo lỗi nếu có
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    progressBar.visibility = View.INVISIBLE
                    btnReset.visibility = View.VISIBLE
                    Toast.makeText(this, "Failed to send reset email: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            edtEmail.error = "Email field can't be empty"
        }
    }

}