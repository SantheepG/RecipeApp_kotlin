package com.example.mobilecw2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe (
    @PrimaryKey val id: Int,
    val meal: String,
    val drinkAlternate:String?,
    val category:String?,
    val area:String?,
    val instructions : String?,
    val mealThumb:String?,
    val tags: String?,
    val youtube: String?,
    val ingredients: List<String>?,
    val measures: List<String>?,
    val src: String?,
    val imgSrc: String?,
    val CreativeCommonsConfirmed:String?,
    val dateModified: String?
    )