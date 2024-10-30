package com.example.ticketbooking.Adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketbooking.Models.Message
import com.example.ticketbooking.R

class ChatAdapter(private val messages: MutableList<Message>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userMessageTextView: TextView = itemView.findViewById(R.id.userMessageTextView)
        private val botMessageTextView: TextView = itemView.findViewById(R.id.botMessageTextView)

        fun bind(message: Message) {
            if (message.isUser) {
                userMessageTextView.text = message.text
                userMessageTextView.visibility = View.VISIBLE
                userMessageTextView.layoutParams = (userMessageTextView.layoutParams as LinearLayout.LayoutParams).apply {
                    gravity = Gravity.END // Căn phải cho tin nhắn người dùng
                }
                botMessageTextView.visibility = View.GONE
            } else {
                // Xử lý tin nhắn của bot, thay thế *** thành ** để in đậm
                val formattedText = message.text.replace(Regex("\\*\\*\\*(.*?)\\*\\*\\*"), "**$1**") // Thay thế ***text*** thành **text**
                botMessageTextView.text = formattedText
                botMessageTextView.visibility = View.VISIBLE
                botMessageTextView.layoutParams = (botMessageTextView.layoutParams as LinearLayout.LayoutParams).apply {
                    gravity = Gravity.START // Căn trái cho tin nhắn chatbot
                }
                userMessageTextView.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}
