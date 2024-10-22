package com.example.ticketbooking.Activity
import android.os.Handler
import android.os.Looper
import android.content.Intent
import android.os.Bundle
import android.util.Log // Thêm dòng này
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ticketbooking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var loginTextView: TextView
    private lateinit var messageTextView: TextView

    companion object {
        private const val TAG = "SignUpActivity" // Thêm tag để log
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        auth = FirebaseAuth.getInstance()
        loginTextView = findViewById(R.id.loginTextView)

        loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun signUp(view: View) {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (signInMethods != null && signInMethods.isNotEmpty()) {
                        Toast.makeText(this, "Email is already in use", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Sign up failed: Email already in use") // Log lỗi
                    } else {
                        registerUser(email, password)
                    }
                } else {
                    Toast.makeText(this, "Error checking email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Error checking email: ${task.exception?.message}") // Log lỗi
                }
            }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                val messageTextView: TextView = findViewById(R.id.messageTextView) // Lấy TextView
                if (task.isSuccessful) {
                    // Đăng ký thành công
                    val user: FirebaseUser? = auth.currentUser
                    messageTextView.text = "Registration successful: ${user?.email}" // Hiển thị thông báo
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this, LoginActivity::class.java) // Đảm bảo bạn đã khai báo LoginActivity trong AndroidManifest
                        startActivity(intent)
                        finish() // Kết thúc SignUpActivity để không trở lại được
                    }, 3000)
                } else {
                    // Nếu đăng ký không thành công, hiển thị thông báo lỗi
                    messageTextView.text = "Registration failed: ${task.exception?.message}" // Hiển thị thông báo
                }
            }
    }

    private fun showMessage(message: String) {
        messageTextView.text = message // Cập nhật TextView với thông báo
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show() // Hiển thị Toast thông báo
    }
}
