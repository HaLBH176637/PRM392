package com.example.ticketbooking.Models

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.io.Serializable

data class User(
    val id: String = "",
    val fullname: String = "",
    val email: String = "",
    val phone: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val address: String = "",
    val password: String = "",
    val role: String = "user"
) : Serializable {

    companion object {
        private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

        // Lưu thông tin người dùng vào Firebase
        fun saveUser(user: User) {
            database.child(user.id).setValue(user)
                .addOnSuccessListener {
                    println("User saved successfully.")
                }
                .addOnFailureListener { error ->
                    println("Failed to save user: ${error.message}")
                }
        }

        // Lấy thông tin người dùng theo ID
        fun getUserById(userId: String, callback: (User?) -> Unit) {
            database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        callback(user)
                    } else {
                        println("No user found with ID: $userId")
                        callback(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Error fetching user: ${error.message}")
                    callback(null)
                }
            })
        }
    }
}
