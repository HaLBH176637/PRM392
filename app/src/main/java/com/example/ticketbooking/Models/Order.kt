package com.example.ticketbooking.Models

data class Order(
    val id: Long,
    val film: Film,
    val selectedSeats: List<String>,
    val date: String,
    val time: String,
    val price: Double,
    val userId: String
)
