package com.example.melodra

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.melodra.adapter.SongsListAdapter
import com.example.melodra.databinding.ActivitySongsListBinding
import com.example.melodra.models.CategoryModel

class SongsListActivity : AppCompatActivity() {
    companion object{
        lateinit var category:CategoryModel
    }

    lateinit var binding: ActivitySongsListBinding
    lateinit var songsListAdapter: SongsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide() // Hide the action bar
        binding = ActivitySongsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        binding=ActivitySongsListBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.nameTextView.text = category.name

        Glide.with(binding.coverImageView.context)
            .load(category.coverURL)
            .apply(RequestOptions().transform(RoundedCorners(30)))
            .into(binding.coverImageView)

        setupSongsListRecyclerView()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }
    }
    fun setupSongsListRecyclerView(){
        songsListAdapter= SongsListAdapter(category.songs)
        binding.songsListRecyclerView.layoutManager=LinearLayoutManager(this)
        binding.songsListRecyclerView.adapter=songsListAdapter
    }
}