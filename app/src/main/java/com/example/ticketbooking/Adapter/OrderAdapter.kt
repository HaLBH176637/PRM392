package com.example.ticketbooking.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketbooking.Models.Order
import com.example.ticketbooking.R

class OrderAdapter(private val orders: List<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFilmTitle: TextView = itemView.findViewById(R.id.tvFilmTitle)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvSelectedSeats: TextView = itemView.findViewById(R.id.tvSelectedSeats)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.tvFilmTitle.text = order.film.Title ?: "Unknown" // Lấy thuộc tính Title từ đối tượng Film
        holder.tvDate.text = "Ngày: ${order.date}"
        holder.tvTime.text = "Thời gian: ${order.time}"
        holder.tvPrice.text = "Giá vé: ${order.price} VND"
        holder.tvSelectedSeats.text = "Ghế đã chọn: ${order.selectedSeats.joinToString(", ")}"
    }

    override fun getItemCount() = orders.size
}
