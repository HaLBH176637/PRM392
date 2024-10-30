package com.example.ticketbooking.Models

data class Message(
    var text: String = "",  // Thêm giá trị mặc định
    var isUser: Boolean = false,
    val timestamp: Long = System.currentTimeMillis() // Giá trị mặc định là thời gian hiện tại
)
