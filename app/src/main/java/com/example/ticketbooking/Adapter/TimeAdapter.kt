package com.example.ticketbooking.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketbooking.R
import com.example.ticketbooking.databinding.ItemTimeBinding

class TimeAdapter(
    private val timeSlots: List<String>,
    private val onTimeSelected: (String) -> Unit // Callback khi người dùng chọn thời gian
) : RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    private var selectedPosition = -1

    inner class TimeViewHolder(private val binding: ItemTimeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(time: String, position: Int) {
            binding.TextViewTime.text = time
            if (selectedPosition == position) {
                binding.TextViewTime.setBackgroundResource(R.drawable.white_bg)
                binding.TextViewTime.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            } else {
                binding.TextViewTime.setBackgroundResource(R.drawable.light_black_bg)
                binding.TextViewTime.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            }

            binding.root.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged()
                onTimeSelected(time) // Trả về thời gian đã chọn
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val binding = ItemTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.bind(timeSlots[position], position)
    }

    override fun getItemCount(): Int = timeSlots.size

    fun getSelectedTime(): String? {
        return if (selectedPosition != -1) timeSlots[selectedPosition] else null
    }
}
