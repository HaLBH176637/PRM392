package com.example.ticketbooking.Activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketbooking.Models.Message
import com.example.ticketbooking.R
import com.example.ticketbooking.Adapter.ChatAdapter
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.database.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ChatBotActivity : AppCompatActivity() {
    private val apiKey = "AIzaSyC9zgmqw5ZHLujwagLYW4BgEF1yRad4XT8" // API Key của bạn
    private lateinit var editTextInput: EditText
    private lateinit var recyclerViewChat: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var typingIndicator: TextView
    private val messages = mutableListOf<Message>()
    private lateinit var database: DatabaseReference
    private lateinit var userId: String // Biến để lưu userId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chatbot)

        // Nhận userId từ Intent
        userId = intent.getStringExtra("userId") ?: ""

        // Khởi tạo các thành phần giao diện
        editTextInput = findViewById(R.id.editTextInput)
        recyclerViewChat = findViewById(R.id.recyclerViewChat)
        typingIndicator = findViewById(R.id.typingIndicator)

        // Cài đặt RecyclerView
        recyclerViewChat.layoutManager = LinearLayoutManager(this)

        // Khởi tạo ChatAdapter
        chatAdapter = ChatAdapter(messages)
        recyclerViewChat.adapter = chatAdapter

        // Khởi tạo Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("chatHistory")

        // Tải lịch sử trò chuyện từ Firebase
        loadChatHistory()

        // Cấu hình nút bấm
        val buttonCallGeminiAI = findViewById<Button>(R.id.buttonCallGeminiAI)
        buttonCallGeminiAI.setOnClickListener { handleSendMessage() }
    }

    private fun loadChatHistory() {
        val userMessagesRef = database.child(userId).child("messages") // Lấy lịch sử của người dùng cụ thể
        userMessagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear() // Xóa danh sách hiện tại để tránh trùng lặp
                for (data in snapshot.children) {
                    val message = data.getValue(Message::class.java)
                    message?.let { messages.add(it) } // Thêm tin nhắn vào danh sách
                }
                chatAdapter.notifyDataSetChanged() // Cập nhật giao diện
                if (messages.isNotEmpty()) {
                    recyclerViewChat.scrollToPosition(messages.size - 1) // Cuộn xuống cuối
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatBotActivity, "Không thể tải lịch sử trò chuyện.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleSendMessage() {
        val question = editTextInput.text.toString().trim()

        if (question.isNotEmpty()) {
            // Lưu tin nhắn của người dùng
            val userMessage = Message(question, true, System.currentTimeMillis())
            messages.add(userMessage)
            chatAdapter.notifyItemInserted(messages.size - 1)
            recyclerViewChat.scrollToPosition(messages.size - 1)

            typingIndicator.visibility = TextView.VISIBLE

            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash",
                apiKey = apiKey
            )

            MainScope().launch {
                try {
                    // Gửi câu hỏi đến AI và nhận phản hồi
                    val response = generativeModel.generateContent(question)

                    typingIndicator.visibility = TextView.GONE

                    // Lấy phản hồi và xử lý văn bản
                    var responseText = response.text ?: "Không có phản hồi từ AI."

                    // Xóa ký tự '*' không mong muốn
                    responseText = responseText.replace("*", "").trim()

                    // Lưu phản hồi của chatbot
                    val botMessage = Message(responseText, false, System.currentTimeMillis())
                    messages.add(botMessage)
                    chatAdapter.notifyItemInserted(messages.size - 1)
                    recyclerViewChat.scrollToPosition(messages.size - 1)

                    // Lưu lịch sử trò chuyện vào Firebase
                    saveChatHistory(userMessage, botMessage)

                    // Xóa nội dung trong EditText sau khi gửi
                    editTextInput.text.clear() // Xóa nội dung
                } catch (e: Exception) {
                    typingIndicator.visibility = TextView.GONE
                    Toast.makeText(this@ChatBotActivity, "Đã xảy ra lỗi khi gọi AI: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Vui lòng nhập câu hỏi.", Toast.LENGTH_SHORT).show()
        }
    }



    private fun saveChatHistory(userMessage: Message, botMessage: Message) {
        val userMessagesRef = database.child(userId).child("messages")

        // Lưu tin nhắn của người dùng
        val userMessageId = userMessagesRef.push().key
        userMessageId?.let {
            userMessagesRef.child(it).setValue(userMessage)
        }

        // Lưu phản hồi của chatbot
        userMessagesRef.push().setValue(botMessage)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(this, "Không thể lưu phản hồi vào lịch sử.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
