
package com.example.ticketbooking.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketbooking.Adapter.OrderAdapter
import com.example.ticketbooking.Models.Film
import com.example.ticketbooking.Models.Order
import com.example.ticketbooking.R
import com.example.ticketbooking.databinding.ActivityMainBinding
import com.example.ticketbooking.databinding.ActivityOrderDetailBinding
import com.example.ticketbooking.databinding.ActivityProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ismaeldivita.chipnavigation.ChipNavigationBar


class OrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var recyclerViewOrders: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var ordersList: MutableList<Order>
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViewOrders = findViewById(R.id.recyclerViewOrders)
        ordersList = mutableListOf()
        orderAdapter = OrderAdapter(ordersList)

        recyclerViewOrders.layoutManager = LinearLayoutManager(this)
        recyclerViewOrders.adapter = orderAdapter

        userId = intent.getStringExtra("USER_ID") ?: ""

        getOrderDetails(userId)

//        val bottomNav = binding.bottomNav // Use binding
//        bottomNav.setOnItemSelectedListener { id ->
//            when (id) {
//                R.id.profile -> {
//                    val intent = Intent(this, ProfileActivity::class.java)
//                    startActivity(intent)
//                }
//                R.id.favorites -> {
//                    // Add logic for favorites if needed
//                }
//                R.id.menu -> {
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            }
//        }
        binding.backBtn.setOnClickListener {
            finish() // Quay lại activity trước đó
        }
    }

    private fun getOrderDetails(userId: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Orders")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var orderFound = false

                for (dateSnapshot in snapshot.children) {
                    for (timeSnapshot in dateSnapshot.children) {
                        for (orderSnapshot in timeSnapshot.children) {
                            val orderUserId = orderSnapshot.child("userId").getValue(String::class.java) ?: ""

                            if (orderUserId == userId) {
                                orderFound = true
                                val filmTitle = orderSnapshot.child("filmTitle").getValue(String::class.java) ?: ""
                                val film = Film(Title = filmTitle) // Tạo đối tượng Film với tên phim
                                val date = orderSnapshot.child("date").getValue(String::class.java) ?: ""
                                val time = orderSnapshot.child("time").getValue(String::class.java) ?: ""
                                val price = orderSnapshot.child("price").getValue(Double::class.java) ?: 0.0
                                val id = orderSnapshot.child("id").getValue(Long::class.java) ?: 0L

                                val selectedSeats = mutableListOf<String>()
                                orderSnapshot.child("selectedSeats").children.forEach { seatSnapshot ->
                                    selectedSeats.add(seatSnapshot.getValue(String::class.java) ?: "")
                                }

                                // Thêm đơn hàng vào danh sách và cập nhật adapter
                                ordersList.add(Order(id, film, selectedSeats, date, time, price, orderUserId))
                                orderAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }

                if (!orderFound) {
                    Toast.makeText(this@OrderDetailActivity, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@OrderDetailActivity, "Lỗi: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
