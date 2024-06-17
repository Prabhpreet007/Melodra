package com.example.melodra.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.melodra.MyExoplayer
import com.example.melodra.PlayerActivity
import com.example.melodra.SongsListActivity.Companion.category
import com.example.melodra.databinding.SongListItemRecyclerRowBinding
import com.example.melodra.models.SongModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore

class SongsListAdapter(private val songIdList: List<String>):
    RecyclerView.Adapter<SongsListAdapter.MyViewHolder>() {
    class MyViewHolder(private val binding: SongListItemRecyclerRowBinding):RecyclerView.ViewHolder(binding.root) {
        fun bindData(songId: String) {
            FirebaseFirestore.getInstance().collection("songs")
                .document(songId).get()
                .addOnSuccessListener {
                    val song=it.toObject(SongModel::class.java)
                    song?.apply{
                        binding.songTitleTextView.text=title
                        binding.songSubtitleTextView.text=subtitle

                        Glide.with(binding.songCoverImageView)
                            .load(coverURL)
                            .apply(RequestOptions().transform(RoundedCorners(30)))
                            .into(binding.songCoverImageView)

                        binding.root.setOnClickListener{
                            MyExoplayer.startPlaying(binding.root.context,song)
                            it.context.startActivity(Intent(it.context,PlayerActivity::class.java))
                        }

                }
                }
        }
    }
    //bind data with view


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding=SongListItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songIdList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(songIdList[position])
    }
}