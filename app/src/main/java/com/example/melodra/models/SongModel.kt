package com.example.melodra.models

data class SongModel(
    val id: String,
    val title: String,
    val subtitle :String,
    val url:String,
    val coverURL:String
){
    constructor():this("","","","","")
}
