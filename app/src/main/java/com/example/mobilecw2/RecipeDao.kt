package com.example.mobilecw2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe")
    suspend fun getAll(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(vararg recipe: Recipe)
    @Insert
    suspend fun insertAll(vararg recipes: Recipe)
    @Query("SELECT COUNT(*) FROM recipe")
    suspend fun getCount(): Int
    //@Query("SELECT * FROM recipe WHERE meal LIKE '%' || :meal || '%' COLLATE NOCASE")
    //@Query("SELECT * FROM recipe WHERE meal IN (SELECT value FROM STRING_SPLIT(ingredients, ','))")
    @Query("SELECT * FROM recipe WHERE ingredients LIKE '%' || :meal || '%' COLLATE NOCASE OR meal LIKE '%' || :meal || '%' COLLATE NOCASE")
    suspend fun getRecipe(meal: String): List<Recipe>



}