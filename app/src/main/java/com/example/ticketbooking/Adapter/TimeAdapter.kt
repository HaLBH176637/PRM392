package com.example.ticketbooking.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketbooking.R
import com.example.ticketbooking.databinding.ItemTimeBinding

class TimeAdapter(private val timeSlots: List<String>): RecyclerView.Adapter<TimeAdapter.TimeViewholder>() {
    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    inner class TimeViewholder(private val binding: ItemTimeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(time: String) {
            binding.TextViewTime.text = time
            if (selectedPosition == adapterPosition) {
                binding.TextViewTime.setBackgroundResource(R.drawable.white_bg)
                binding.TextViewTime.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            } else {
                binding.TextViewTime.setBackgroundResource(R.drawable.light_black_bg)
                binding.TextViewTime.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            }

            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    lastSelectedPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(lastSelectedPosition)
                    notifyItemChanged(selectedPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewholder {
        return TimeViewholder(
            ItemTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TimeViewholder, position: Int) {
        holder.bind(timeSlots[position])
    }

    override fun getItemCount(): Int = timeSlots.size

    // Thêm phương thức này để lấy vị trí của thời gian đã chọn
    fun getSelectedPosition(): Int {
        return selectedPosition
    }
    fun getItem(position: Int): String {
        return timeSlots[position]
    }

}
