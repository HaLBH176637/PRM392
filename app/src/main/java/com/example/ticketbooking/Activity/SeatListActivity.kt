package com.example.ticketbooking.Activity

import android.icu.text.DecimalFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ticketbooking.Adapter.DateAdapter
import com.example.ticketbooking.Adapter.SeatListAdapter
import com.example.ticketbooking.Adapter.TimeAdapter
import com.example.ticketbooking.Models.Film
import com.example.ticketbooking.Models.Order
import com.example.ticketbooking.Models.Seat
import com.example.ticketbooking.R
import com.example.ticketbooking.databinding.ActivitySeatListBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class SeatListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeatListBinding
    private lateinit var database: DatabaseReference
    private lateinit var film: Film
    private var price: Double = 0.0
    private var number: Int = 0
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySeatListBinding.inflate(layoutInflater)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", "") ?: ""

        setContentView(binding.root)
        getIntentExtra()
        setVariable()
        initSeatsList()
    }

    private fun initSeatsList() {
        val gridLayoutManager = GridLayoutManager(this, 7)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position % 7 == 3) 1 else 1
            }
        }
        binding.seatRecyclerview.layoutManager = gridLayoutManager

        val seatList = mutableListOf<Seat>()
        val numberSeats = 81
        for (i in 0 until numberSeats) {
            val seatName = "Seat$i"
            val seatStatus = if (i == 2 || i == 20 || i == 33 || i == 41 || i == 50 || i == 72 || i == 73) {
                Seat.SeatStatus.UNAVAILABLE
            } else {
                Seat.SeatStatus.AVAILABLE
            }
            seatList.add(Seat(seatStatus, seatName))
        }

        val seatAdapter = SeatListAdapter(seatList, this, object : SeatListAdapter.SelectedSeat {
            override fun Returns(selectedName: String, num: Int) {
                binding.numberSelectedTxt.text = "$num Seat Selected"
                val df = DecimalFormat("#.##")
                price = df.format(num * film.price).toDouble()
                number = num
                binding.priceTxt.text = "$$price"
            }
        })

        binding.seatRecyclerview.adapter = seatAdapter
        binding.seatRecyclerview.isNestedScrollingEnabled = false

        binding.TimeRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.TimeRecyclerview.adapter = TimeAdapter(generateTimeSlots())

        binding.dateRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.dateRecyclerview.adapter = DateAdapter(generateDates())
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.processPaymentBtn.setOnClickListener {
            processPayment()
        }
    }

    private fun getIntentExtra() {
        film = intent.getParcelableExtra("film")!!
    }

    private fun generateTimeSlots(): List<String> {
        val timeSlots = mutableListOf<String>()
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")

        for (i in 0 until 24 step 2) {
            val time = LocalTime.of(i, 0)
            timeSlots.add(time.format(formatter))
        }
        return timeSlots
    }

    private fun generateDates(): List<String> {
        val dates = mutableListOf<String>()
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEE/dd/MMM")

        for (i in 0 until 7) {
            dates.add(today.plusDays(i.toLong()).format(formatter))
        }
        return dates
    }

    private fun processPayment() {
        val selectedTimePosition = (binding.TimeRecyclerview.adapter as TimeAdapter).getSelectedPosition()
        if (selectedTimePosition == -1) {
            Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedTime = (binding.TimeRecyclerview.adapter as TimeAdapter).getItem(selectedTimePosition)
        val selectedDate = binding.selectedDateTextView.text.toString() // Thay đổi ID này phù hợp với layout của bạn

        val order = Order(
            id = generateOrderId(), // Hàm để tạo ID cho đơn hàng
            film = film,
            selectedSeats = listOf("Seat1", "Seat2"), // Thay đổi danh sách ghế đã chọn
            date = selectedDate,
            time = selectedTime,
            price = price,
            userId = userId // Sử dụng userId đã lấy từ SharedPreferences
        )

        // Lưu đơn hàng vào Firebase
        database = FirebaseDatabase.getInstance().reference.child("Orders")
        database.child(order.id.toString()).setValue(order)
            .addOnSuccessListener {
                // Xử lý thành công, ví dụ: hiện thông báo
                Toast.makeText(this, "Đặt vé thành công!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Xử lý thất bại, ví dụ: hiện thông báo lỗi
                Toast.makeText(this, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun generateOrderId(): Int {
        // Tạo ID đơn hàng duy nhất (có thể là số ngẫu nhiên hoặc tăng dần)
        return (System.currentTimeMillis() % 100000).toInt() // Chỉ là ví dụ, bạn có thể thay đổi cách tạo ID
    }
}
