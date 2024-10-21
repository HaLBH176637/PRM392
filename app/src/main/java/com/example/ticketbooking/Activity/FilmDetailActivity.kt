package com.example.ticketbooking.Activity

import android.content.ClipData.Item
import android.content.Intent
import android.os.Bundle
import android.renderscript.RenderScript
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.ticketbooking.Adapter.CastListAdapter
import com.example.ticketbooking.Adapter.CategoryEachFilmAdapter
import com.example.ticketbooking.Models.Film
import com.example.ticketbooking.R
import com.example.ticketbooking.databinding.ActivityFilmDetailBinding
import com.example.ticketbooking.databinding.ActivityMainBinding
import eightbitlab.com.blurview.RenderScriptBlur

class FilmDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilmDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFilmDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setVariable()
    }

    private fun setVariable() {
        val item: Film = intent.getParcelableExtra("object")!!
        val requestOptions = RequestOptions().transform(CenterCrop(),GranularRoundedCorners(0f,0f,50f,50f))
        Glide.with(this)
            .load(item.Poster)
            .apply(requestOptions)
            .into(binding.filmPic)
        binding.titleTxt.text=item.Title
        binding.imdbTxt.text="IMDB ${item.Imdb}"
        binding.movieTimeTxt.text = "${item.Year} - ${item.Time}"
        binding.movieSummeryTxt.text = item.Description
        binding.backBtn.setOnClickListener{
            finish()
        }
        binding.buyTicketBtn.setOnClickListener{
            val intent =Intent(this,SeatListActivity::class.java)
            intent.putExtra("film",item)
            startActivity(intent)
        }
        val radius = 10f
        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowBackground = decorView.background

        binding.blurView.setupWith(rootView,RenderScriptBlur(this))
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(radius)
        binding.blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
        binding.blurView.clipToOutline = true


        item.Genre.let{
            binding.gerneView.adapter=CategoryEachFilmAdapter(it)
            binding.gerneView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        }
        item.Casts?.let {
            binding.castListView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
            binding.castListView.adapter=CastListAdapter(it)
        }
    }
}