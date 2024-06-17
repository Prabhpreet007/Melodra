package com.example.melodra.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.melodra.SongsListActivity
import com.example.melodra.databinding.CategoryItemRecyclerRowBinding
import com.example.melodra.models.CategoryModel

class CategoryAdapter(var categoryList: List<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    class MyViewHolder( val binding: CategoryItemRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(category: CategoryModel) {
            // Set the text of the TextView
            binding.nameTextView.text = category.name
            // Load the image using Glide
            Glide.with(binding.coverImageView.context)
                .load(category.coverURL)
                .apply(RequestOptions().transform(RoundedCorners(30)))
                .into(binding.coverImageView)

            //Songs
            val context=binding.root.context
            binding.root.setOnClickListener{
                SongsListActivity.category=category
                context.startActivity(Intent(context,SongsListActivity::class.java))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CategoryItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(categoryList[position])
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}
