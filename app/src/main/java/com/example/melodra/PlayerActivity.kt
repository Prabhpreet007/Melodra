package com.example.melodra

import android.os.Bundle
import android.view.View

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat

import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.extractor.text.Subtitle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.melodra.databinding.ActivityMainBinding
import com.example.melodra.databinding.ActivityPlayerBinding
//import com.google.firebase.firestore.core.View

class PlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivityPlayerBinding
    lateinit var exoPlayer:ExoPlayer

    var playerListener=object : Player.Listener{
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            showGif(isPlaying)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        supportActionBar?.hide() // Hide the action bar
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding=ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyExoplayer.getCurrentSong()?.apply {
            binding.songTitleTextView.text=title
            binding.songSubtitleTextView.text=subtitle

            Glide.with(binding.songCoverImageView)
                .load(coverURL)
                .circleCrop()
                .into(binding.songCoverImageView)

            Glide.with(binding.songGifImageView)
                .load(R.drawable.media_playing)
                .circleCrop()
                .into(binding.songGifImageView)

            exoPlayer=MyExoplayer.getInstance()!!
            binding.playerView.player=exoPlayer

            binding.playerView.showController()

            exoPlayer.addListener(playerListener)

        }

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.removeListener(playerListener)
    }

    fun showGif(show:Boolean){
        if(show) {
            binding.songGifImageView.visibility = View.VISIBLE
        }else{
            binding.songGifImageView.visibility=View.INVISIBLE
    }}

}