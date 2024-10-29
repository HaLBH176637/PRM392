package com.example.ticketbooking.Activity

import android.content.ClipData.Item
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.ticketbooking.Adapter.FilmListAdapter
import com.example.ticketbooking.Adapter.SliderAdapter
import com.example.ticketbooking.Models.Film
import com.example.ticketbooking.Models.SliderItems
import com.example.ticketbooking.R
import com.example.ticketbooking.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ismaeldivita.chipnavigation.ChipNavigationBar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var emailuserTxt: TextView
    private lateinit var fullnameTxt: TextView
    private val sliderHandle=Handler()
    private val sliderRunnable = Runnable {
        binding.viewPager2.currentItem = binding.viewPager2.currentItem + 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize database
        database = FirebaseDatabase.getInstance()


        // Lấy thông tin người dùng từ SharedPreferences
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", null)
        val userFullName = sharedPreferences.getString("userFullName", null)
        val userId = sharedPreferences.getString("userId", null) ?: "defaultUserId"
        // Hiển thị thông tin nếu có
        emailuserTxt = binding.emailuserTxt // Use binding
        fullnameTxt = binding.fullnameTxt // Use binding

        emailuserTxt.text = userEmail ?: "Email không có"
        fullnameTxt.text = userFullName ?: "Tên không có"

        // Các phần khởi tạo khác
        initTopMoving()
        initUpcomming()
        initBanner()
        val bottomNav = binding.bottomNav // Use binding
        bottomNav.setOnItemSelectedListener { id ->
            when (id) {
                R.id.profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
                R.id.favorites -> {
                    // Add logic for favorites if needed
                }
                R.id.cart -> {
                    // Chuyển tới OrderDetailActivity và truyền userId
                    val intent = Intent(this, OrderDetailActivity::class.java)
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                }
            }
        }

    }


    private fun initTopMoving() {
        val myRef: DatabaseReference = database.reference.child("Items")
        binding.progressBarTopMovies.visibility = View.VISIBLE
        val items = ArrayList<Film>()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (issue in snapshot.children) {
                        items.add(issue.getValue(Film::class.java)!!)
                    }
                    if (items.isNotEmpty()) {
                        binding.recyclerViewTopMovies.layoutManager = LinearLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                    }
                    binding.recyclerViewTopMovies.adapter = FilmListAdapter(items)
                }
                binding.progressBarTopMovies.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu cần
            }
        })
    }

    private fun initUpcomming() {
        val myRef: DatabaseReference = database.reference.child("Upcomming")
        binding.progressBarupcomming.visibility = View.VISIBLE
        val items = ArrayList<Film>()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (issue in snapshot.children) {
                        items.add(issue.getValue(Film::class.java)!!)
                    }
                    if (items.isNotEmpty()) {
                        binding.recyclerViewUpcomming.layoutManager = LinearLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                    }
                    binding.recyclerViewUpcomming.adapter = FilmListAdapter(items)
                }
                binding.progressBarupcomming.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu cần
            }
        })
    }
    private fun initBanner() {
        val myRef: DatabaseReference=database.getReference("Banners")
        binding.progressBarSlider.visibility = View.VISIBLE
        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderItems>()
                for (childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(SliderItems::class.java)
                    if(list!=null){
                        lists.add(list)
                    }
                }
                binding.progressBarSlider.visibility= View.GONE
                banners(lists)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun banners(lists: MutableList<SliderItems>) {
        binding.viewPager2.adapter=SliderAdapter(lists,binding.viewPager2)
        binding.viewPager2.clipToPadding=false
        binding.viewPager2.clipChildren=false
        binding.viewPager2.offscreenPageLimit=3
        binding.viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer(ViewPager2.PageTransformer{ page, position ->
                val r = 1 - Math.abs(position)
                page.scaleY = 0.85f + r * 0.15f
            })
        }
        binding.viewPager2.setPageTransformer(compositePageTransformer)
        binding.viewPager2.currentItem = 1
        binding.viewPager2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandle.removeCallbacks(sliderRunnable)
                sliderHandle.postDelayed(sliderRunnable, 2000)
            }
        })


    }

    override fun onPause() {
        super.onPause()
        sliderHandle.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandle.postDelayed(sliderRunnable, 2000)
    }
}