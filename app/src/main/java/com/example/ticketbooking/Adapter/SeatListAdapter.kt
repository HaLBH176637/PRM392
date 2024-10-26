package com.example.ticketbooking.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketbooking.Models.Seat
import com.example.ticketbooking.R
import com.example.ticketbooking.databinding.SeatItemBinding

class SeatListAdapter(
    private val seatList: List<Seat>,
    private val context: Context,
    private val selectedSeat: SelectedSeat,
    private val bookedSeats: List<String> // Danh sách ghế đã đặt
) : RecyclerView.Adapter<SeatListAdapter.SeatViewHolder>() {

    private val selectedSeatNames = ArrayList<String>()

    inner class SeatViewHolder(val binding: SeatItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(seat: Seat) {
            // Hiển thị tên ghế từ Firebase
            binding.seat.text = seat.name

            // Thay đổi trạng thái ghế
            when {
                bookedSeats.contains(seat.name) -> {
                    binding.seat.setBackgroundResource(R.drawable.ic_seat_unavailable) // Ghế đã đặt
                    binding.seat.setTextColor(context.getColor(R.color.grey))
                    binding.seat.isClickable = false // Không cho phép click
                }
                seat.SeatStatus == "AVAILABLE" -> {
                    binding.seat.setBackgroundResource(R.drawable.ic_seat_available)
                    binding.seat.setTextColor(context.getColor(R.color.white))
                    binding.seat.isClickable = true // Cho phép click
                }
                seat.SeatStatus == "UNAVAILABLE" -> {
                    binding.seat.setBackgroundResource(R.drawable.ic_seat_unavailable)
                    binding.seat.setTextColor(context.getColor(R.color.grey))
                    binding.seat.isClickable = true // Cho phép click để chọn lại
                }
            }

            // Xử lý click
            binding.seat.setOnClickListener {
                if (seat.SeatStatus == "AVAILABLE") {
                    seat.SeatStatus = "UNAVAILABLE"
                    selectedSeatNames.add(seat.name)
                } else if (seat.SeatStatus == "UNAVAILABLE") {
                    seat.SeatStatus = "AVAILABLE"
                    selectedSeatNames.remove(seat.name)
                }
                notifyItemChanged(adapterPosition) // Cập nhật trạng thái ghế
                val selected = selectedSeatNames.joinToString(", ")
                selectedSeat.Returns(selected, selectedSeatNames.size) // Gọi callback
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val binding = SeatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        holder.bind(seatList[position])
    }

    override fun getItemCount(): Int = seatList.size

    // Phương thức để reset danh sách ghế đã chọn sau khi đặt vé
    fun clearSelectedSeats() {
        selectedSeatNames.clear()
        notifyDataSetChanged() // Làm mới lại RecyclerView
    }

    interface SelectedSeat {
        fun Returns(selectedName: String, number: Int)
    }
}
