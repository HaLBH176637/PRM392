package com.example.ticketbooking.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ticketbooking.Adapter.DateAdapter
import com.example.ticketbooking.Adapter.SeatListAdapter
import com.example.ticketbooking.Adapter.TimeAdapter
import com.example.ticketbooking.Models.Film
import com.example.ticketbooking.Models.Seat
import com.example.ticketbooking.databinding.ActivitySeatListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

    class SeatListActivity : AppCompatActivity(), SeatListAdapter.SelectedSeat {

        private lateinit var binding: ActivitySeatListBinding
        private lateinit var database: DatabaseReference
        private lateinit var seatListAdapter: SeatListAdapter
        private var selectedSeats = ArrayList<String>()
        private var selectedDate = ""
        private var selectedTime = ""
        private var price = 45000 // Giá vé giả định
        private var currentTotalPrice = 0 // Biến để lưu giá trị tổng cộng hiện tại
        private var userId: String? = null
        private val bookedSeats = mutableListOf<String>() // Danh sách ghế đã đặt
        private lateinit var film: Film // Thêm biến film

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivitySeatListBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Lấy userId từ SharedPreferences
            val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            userId = sharedPreferences.getString("userId", null)

            if (userId == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

            // Nhận thông tin film từ Intent
            film = intent.getParcelableExtra("film") ?: Film() // Khởi tạo film rỗng nếu không có

            database = FirebaseDatabase.getInstance().reference
            loadDates()
            loadTimes()
            loadBookedSeats() // Tải ghế đã đặt
            binding.processPaymentBtn.setOnClickListener {
                if (selectedSeats.isNotEmpty() && selectedDate.isNotBlank() && selectedTime.isNotBlank()) {
                    saveOrderToDatabase()
                } else {
                    Toast.makeText(this, "Hãy chọn ngày, thời gian và ghế.", Toast.LENGTH_SHORT).show()
                }
            }
            binding.backBtn.setOnClickListener {
                finish()
            }
        }

        private fun loadBookedSeats() {
            // Tải danh sách ghế đã đặt từ Firebase
            database.child("Orders").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (orderSnapshot in snapshot.children) {
                        val orderData = orderSnapshot.value as? Map<*, *>
                        if (orderData != null) {
                            val seats = orderData["selectedSeats"] as? List<String> ?: continue
                            bookedSeats.addAll(seats.map { it.toString().trim() }) // Thêm ghế vào danh sách đã đặt
                        }
                    }
                    // Bây giờ bạn có thể tải ghế
                    loadSeats()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@SeatListActivity, "Error loading booked seats", Toast.LENGTH_SHORT).show()
                }
            })
        }

        private fun loadSeats() {
            selectedSeats.clear() // Xóa các ghế đã chọn mỗi khi tải lại ghế

            if (selectedDate.isBlank() || selectedTime.isBlank()) {
                Toast.makeText(this, "Hãy chọn ngày và giờ.", Toast.LENGTH_SHORT).show()
                return
            }

            // Tải ghế từ Firebase với ngày và giờ đã chọn
            database.child("Seats").child(selectedDate).child(selectedTime).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val seats = mutableListOf<Seat>()
                    for (seatSnapshot in snapshot.children) {
                        val seatData = seatSnapshot.value as? Map<*, *>
                        if (seatData != null) {
                            val seatName = seatData["name"] as? String ?: continue
                            // Kiểm tra xem ghế có nằm trong danh sách bookedSeats không
                            val seatStatus = if (bookedSeats.contains(seatName.trim())) "UNAVAILABLE" else seatData["SeatStatus"] as? String ?: "AVAILABLE"

                            // Thêm ghế vào danh sách
                            seats.add(Seat(name = seatName, SeatStatus = seatStatus))
                        }
                    }

                    if (seats.isEmpty()) {
                        Toast.makeText(this@SeatListActivity, "Không có ghế nào được tìm thấy.", Toast.LENGTH_SHORT).show()
                    }

                    // Hiển thị danh sách ghế
                    binding.seatRecyclerview.layoutManager = GridLayoutManager(this@SeatListActivity, 7)
                    seatListAdapter = SeatListAdapter(seats, this@SeatListActivity, this@SeatListActivity, bookedSeats)
                    binding.seatRecyclerview.adapter = seatListAdapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@SeatListActivity, "Error loading seats", Toast.LENGTH_SHORT).show()
                }
            })
        }

        private fun loadDates() {
        database.child("Dates").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dates = snapshot.children.map { it.getValue(String::class.java)!! }
                binding.dateRecyclerview.layoutManager = LinearLayoutManager(this@SeatListActivity, LinearLayoutManager.HORIZONTAL, false)
                binding.dateRecyclerview.adapter = DateAdapter(dates) { date ->
                    selectedDate = date
                    binding.selectedDateTextView.text = "Selected Date: $date"
                    loadSeats() // Tải lại ghế khi chọn ngày
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SeatListActivity, "Error loading dates", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadTimes() {
        database.child("Times").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val times = snapshot.children.map { it.getValue(String::class.java)!! }
                binding.TimeRecyclerview.layoutManager = LinearLayoutManager(this@SeatListActivity, LinearLayoutManager.HORIZONTAL, false)
                binding.TimeRecyclerview.adapter = TimeAdapter(times) { time ->
                    selectedTime = time
                    binding.selectedTimeTextView.text = "Selected Time: $time"
                    loadSeats() // Tải lại ghế khi chọn thời gian
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SeatListActivity, "Error loading times", Toast.LENGTH_SHORT).show()
            }
        })
    }
        private fun saveOrderToDatabase() {
            // Kiểm tra xem tất cả ghế đã chọn có phải là "AVAILABLE" không
            if (selectedSeats.any { bookedSeats.contains(it) }) {
                Toast.makeText(this, "Một hoặc nhiều ghế đã được đặt trước.", Toast.LENGTH_SHORT).show()
                return
            }

            // Tạo thông tin đơn hàng dưới dạng MutableMap
            val order = mutableMapOf<String, Any>(
                "id" to System.currentTimeMillis(),
                "filmTitle" to (film.Title ?: ""), // Sử dụng giá trị mặc định nếu Title là null
                "selectedSeats" to selectedSeats,
                "date" to selectedDate,
                "time" to selectedTime,
                "price" to currentTotalPrice,
                "userId" to (userId ?: "")
            )



            // Lưu vào đường dẫn tương ứng với ngày và giờ
            database.child("Orders").child(selectedDate).child(selectedTime).push().setValue(order).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Cập nhật trạng thái ghế thành "UNAVAILABLE"
                    updateSeatStatus("UNAVAILABLE")
                    Toast.makeText(this, "Đặt vé thành công!", Toast.LENGTH_SHORT).show()
                    resetSelection()

                    // Chuyển về MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Đóng SeatListActivity
                } else {
                    Toast.makeText(this, "Đặt vé thất bại!", Toast.LENGTH_SHORT).show()
                }
            }
        }



        private fun updateSeatStatus(status: String) {
        for (seatName in selectedSeats) {
            // Cập nhật trạng thái ghế
            val seatUpdate = mutableMapOf<String, Any>(
                "SeatStatus" to status
            )
            // Chú ý đến cấu trúc đường dẫn
            database.child("Seats").child(selectedDate).child(selectedTime).child(seatName).updateChildren(seatUpdate)
        }
    }

    private fun resetSelection() {
        selectedSeats.clear()
        currentTotalPrice = 0
        binding.priceTxt.text = "0 VNĐ"
        binding.numberSelectedTxt.text = "0 Seat Selected"
        seatListAdapter.clearSelectedSeats()
        loadSeats() // Tải lại danh sách ghế để cập nhật trạng thái
    }

    override fun Returns(selectedName: String, number: Int) {
        selectedSeats.clear()
        selectedSeats.addAll(selectedName.split(",").filter { it.isNotEmpty() })
        currentTotalPrice = number * price
        binding.numberSelectedTxt.text = "$number Seat Selected"
        binding.priceTxt.text = "${currentTotalPrice} VNĐ"
    }
}
