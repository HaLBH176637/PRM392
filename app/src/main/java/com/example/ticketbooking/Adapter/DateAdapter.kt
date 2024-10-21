package com.example.ticketbooking.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketbooking.R
import com.example.ticketbooking.databinding.ItemDateBinding
import com.example.ticketbooking.databinding.ItemTimeBinding

class DateAdapter(private val timeSlots: List<String>):RecyclerView.Adapter<DateAdapter.TimeViewholder>() {
    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    inner class TimeViewholder (private val binding: ItemDateBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(date:String){
            val dateParts = date.split("/")
            if(dateParts.size==3){
                binding.dayTxt.text=dateParts[0]
                binding.datMonthTxt.text=dateParts[1]+""+dateParts[2]

            if (selectedPosition==position){
                binding.mailLayout.setBackgroundResource(R.drawable.white_bg)
                binding.dayTxt.setTextColor(ContextCompat.getColor(itemView.context,R.color.black))
                binding.datMonthTxt.setTextColor(ContextCompat.getColor(itemView.context,R.color.black))
            }
            else {
                binding.mailLayout.setBackgroundResource(R.drawable.light_black_bg)
                binding.dayTxt.setTextColor(ContextCompat.getColor(itemView.context,R.color.white))
                binding.datMonthTxt.setTextColor(ContextCompat.getColor(itemView.context,R.color.white))
            }
            binding.root.setOnClickListener{
                val  position = position
                if (position!=RecyclerView.NO_POSITION){
                    lastSelectedPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(lastSelectedPosition)
                    notifyItemChanged(selectedPosition)
                }
            }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateAdapter.TimeViewholder {
       return TimeViewholder(
           ItemDateBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       )
    }

    override fun onBindViewHolder(holder: DateAdapter.TimeViewholder, position: Int) {
        holder.bind(timeSlots[position])
    }

    override fun getItemCount(): Int = timeSlots.size
}