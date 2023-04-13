package com.example.mobilecw2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.*

class SearchMeal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchmeal)

        var stb = StringBuilder()
        var tv1 = findViewById<TextView>(R.id.tv1)
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "mydatabase").build()
        val recipeDao = db.recipeDao()
        val search = findViewById<Button>(R.id.searchButton)
        search.setOnClickListener {
            runBlocking {
                launch {
                    withContext(Dispatchers.IO) {
                        val recipes: List<Recipe> = recipeDao.getAll()
                        for (r in recipes) {
                            stb.append("\n ${r.meal}\n ${r.drinkAlternate}\n ${r.category} \n ${r.area}\n ${r.instructions}\n${r.mealThumb}\n${r.tags}\n${r.youtube}\n")
                        }
                    }
                }
            }
            runOnUiThread {
                //updates the UI
                tv1.text = stb
            }
        }





            //tv.append("\n ${r.id}\n ${r.firstName}\n ${r.lastName}")
            //tv.append("\n ${r.Meal}\n ${r.DrinkAlternate}\n${r.Category}\n${r.Area}\n${r.Tags}\n${r.Ingredient1}\n${r.Ingredient2}\n${r.Measure1}\n${r.Measure2}")



    }
}