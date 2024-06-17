//package com.example.melodra
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.melodra.adapter.CategoryAdapter
//import com.example.melodra.databinding.ActivityMainBinding
//import com.example.melodra.models.CategoryModel
//import com.google.firebase.firestore.FirebaseFirestore
//import android.util.Log
//import com.example.melodra.adapter.SectionSongListAdapter
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var categoryAdapter: CategoryAdapter
////    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        supportActionBar?.hide() // Hide the action bar
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//
//        // Initialize RecyclerView
//        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        categoryAdapter = CategoryAdapter(emptyList())
//        binding.categoriesRecyclerView.adapter = categoryAdapter
//
//        // Fetch categories from Firestore
//        getCategories()
//        setupSection()
//    }
//
//    //categories
//
//
//    private fun getCategories() {
////        Log.d("MainActivity", "Fetching categories from Firestore...")
////        firestore.collection("category").get().addOnSuccessListener { querySnapshot ->
////            val categoryList = querySnapshot.toObjects(CategoryModel::class.java)
////
////            if (categoryList.isEmpty()) {
////                Log.d("MainActivity", "No categories found")
////            } else {
////                Log.d("MainActivity", "Categories fetched: ${categoryList.size}")
////            }
////            setCategoryRecyclerView(categoryList)
////        }.addOnFailureListener { exception ->
////            Log.e("MainActivity", "Error getting categories", exception)
////        }
//        FirebaseFirestore.getInstance().collection("category")
//            .get().addOnSuccessListener {
//                val categoryList=it.toObjects(CategoryModel::class.java)
//                setupCategoryRecyclerView(categoryList)
//            }
//    }
//
//    private fun setupCategoryRecyclerView(categoryList: List<CategoryModel>) {
////        Log.d("MainActivity", "Setting up RecyclerView with ${categoryList.size} categories")
//
////        categoryAdapter = CategoryAdapter(categoryList)
//
//        categoryAdapter.categoryList=categoryList
//        binding.categoriesRecyclerView.adapter?.notifyDataSetChanged()
//
//    }
//
//    //Sections
//    fun setupSection(){
//        FirebaseFirestore.getInstance().collection("section")
//            .document("section_1")
//            .get().addOnSuccessListener {
//                val section=it.toObject(CategoryModel::class.java)
//                section?.apply {
//                    binding.section1Title.text=name
//                    binding.section1RecyclerView.layoutManager=LinearLayoutManager(this@MainActivity)
//                    binding.section1RecyclerView.adapter=SectionSongListAdapter(songs)
//
//
//                }
//            }
//    }
//
//}




package com.example.melodra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.InflateException
import android.view.View
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.melodra.adapter.CategoryAdapter
import com.example.melodra.adapter.SectionSongListAdapter
import com.example.melodra.databinding.ActivityMainBinding
import com.example.melodra.models.CategoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var categoryAdapter: CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                supportActionBar?.hide() // Hide the action bar

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCategories()
        setupSection("section_1",binding.section1MainLayout,binding.section1Title,binding.section1RecyclerView)
        setupSection("section_2",binding.section2MainLayout,binding.section2Title,binding.section2RecyclerView)
        setupSection("section_3",binding.section3MainLayout,binding.section3Title,binding.section3RecyclerView)

        binding.optionBtn.setOnClickListener{
            showPopupMenu()
        }
    }

    fun showPopupMenu(){
        val popupMenu=PopupMenu(this,binding.optionBtn)
        val inflator=popupMenu.menuInflater
        inflator.inflate(R.menu.option_menu,popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.logout -> {
                    logout()
                    true
                }
            }
            false

        }

    }

    fun logout(){
        MyExoplayer.getInstance()?.release()
        FirebaseAuth.getInstance().signOut()
        finish()
        startActivity(Intent(this,LoginActivity::class.java))

    }

    override fun onResume(){
        super.onResume()
        showPlayerView()

    }

    fun showPlayerView(){

        binding.playerView.setOnClickListener{
            startActivity(Intent(this,PlayerActivity::class.java))
        }

        MyExoplayer.getCurrentSong()?.let {
            binding.playerView.visibility=View.VISIBLE
            binding.songTitleTextView.text=it.title

            Glide.with(binding.songCoverImageView).load(it.coverURL)
                .apply(RequestOptions().transform(RoundedCorners(32)))
                .into(binding.songCoverImageView)

        }?: run{
            binding.playerView.visibility=View.GONE
        }
    }

     fun getCategories() {
        FirebaseFirestore.getInstance().collection("category").get().addOnSuccessListener {
            val categoryList = it.toObjects(CategoryModel::class.java)
            setupCategoryRecyclerView(categoryList)
        }
    }

     fun setupCategoryRecyclerView(categoryList: List<CategoryModel>) {
        categoryAdapter = CategoryAdapter(categoryList)
        binding.categoriesRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.categoriesRecyclerView.adapter = categoryAdapter
    }

    fun setupSection(id : String,mainLayout: RelativeLayout,titleView:TextView,recyclerView:RecyclerView) {
        FirebaseFirestore.getInstance().collection("sections")
            .document(id)
            .get().addOnSuccessListener {
                val section = it.toObject(CategoryModel::class.java)
                section?.apply {

                    mainLayout.visibility = View.VISIBLE

                    titleView.text = name
                    recyclerView.layoutManager =
                        LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                    recyclerView.adapter = SectionSongListAdapter(songs)
                    mainLayout.setOnClickListener{
                        SongsListActivity.category=section
                        startActivity(Intent(this@MainActivity,SongsListActivity::class.java))

                    }


                }

            }
    }
}
