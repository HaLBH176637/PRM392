package com.example.ticketbooking.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketbooking.R
import com.example.ticketbooking.databinding.ItemDateBinding

class DateAdapter(private val dates: List<String>) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {
    private var selectedPosition = -1

    inner class DateViewHolder(private val binding: ItemDateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(date: String, position: Int) {
            val dateParts = date.split("/")
            if (dateParts.size == 3) {
                binding.dayTxt.text = dateParts[0]
                binding.datMonthTxt.text = dateParts[1] + " " + dateParts[2]

                // Thay đổi màu sắc dựa trên vị trí đã chọn
                if (selectedPosition == position) {
                    binding.mailLayout.setBackgroundResource(R.drawable.white_bg)
                    binding.dayTxt.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                    binding.datMonthTxt.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                } else {
                    binding.mailLayout.setBackgroundResource(R.drawable.light_black_bg)
                    binding.dayTxt.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                    binding.datMonthTxt.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                }

                binding.root.setOnClickListener {
                    if (position != RecyclerView.NO_POSITION) {
                        // Cập nhật vị trí đã chọn và thông báo cho adapter
                        selectedPosition = position
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        return DateViewHolder(
            ItemDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.bind(dates[position], position)
    }

    override fun getItemCount(): Int = dates.size
}
