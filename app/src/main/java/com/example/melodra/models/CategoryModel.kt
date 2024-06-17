package com.example.melodra.models

data class CategoryModel(
    val name:String,
    val coverURL:String,
    val songs:List<String>
){
    constructor(): this("","",listOf())
}
