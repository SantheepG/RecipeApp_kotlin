package com.example.mobilecw2

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.*

class SearchMeal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchmeal)

        // assuming you have a reference to your ScrollView
        val scrollView = findViewById<ScrollView>(R.id.scrollView)

        // create a new LinearLayout with vertical orientation
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL


        //var tv1 = findViewById<TextView>(R.id.tv1)
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "DB8").build()
        val recipeDao = db.recipeDao()
        val search = findViewById<Button>(R.id.searchButton)
        search.setOnClickListener {
            linearLayout.removeAllViews()
            scrollView.removeAllViews()
            //var stb = StringBuilder()
            val input = findViewById<EditText>(R.id.inputText).text.toString()
            if (input != ""){
                runBlocking {
                    withContext(Dispatchers.IO) {
                        val recipes: List<Recipe> = recipeDao.getRecipe(input)
                        updateUI(recipes,linearLayout)
                        //for (r in recipes) {
                            //val imageView = ImageView(this)

                        //    stb.append("\n ${r.meal}\n ${r.drinkAlternate}\n ${r.category} \n ${r.area}\n ${r.instructions}\n${r.mealThumb}\n${r.tags}\n${r.youtube}\n")
                       // }
                    }
                }
            }

            //runOnUiThread {
            scrollView.addView(linearLayout)
            //tv1.text = stb
            //}
        }
    }
    suspend fun updateUI(recipe: List<Recipe>,linearLayout: LinearLayout,){
        for(r in recipe){
            val mealThumb = ImageView(this)
            val mealName = TextView(this)
            val desc = TextView(this)
            val ing = TextView(this)
            val ingMrs = TextView(this)
            val inst = TextView(this)
            val instructions = TextView(this)
            mealName.text = r.meal
            mealName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20F)
            mealName.setTextColor(Color.WHITE)
            mealName.setTypeface(null, Typeface.BOLD)

            desc.text = "Drink alternate: ${r.drinkAlternate}\nCategory: ${r.category}\nArea: ${r.area}\n\n"
            desc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15F)
            desc.setTextColor(Color.WHITE)

            ing.text = "\nIngredients & Measures"
            ing.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15F)
            ing.setTextColor(Color.WHITE)
            ing.setTypeface(null, Typeface.BOLD)

            for (i in 0 until (r.ingredients?.size ?: 0)) {
                ingMrs.append("${r.ingredients?.get(i)}-${r.measures?.get(i)}, ")
            }
            ingMrs.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15F)
            ingMrs.setTextColor(Color.WHITE)

            inst.text = "\nInstructions"
            inst.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15F)
            inst.setTextColor(Color.WHITE)
            inst.setTypeface(null, Typeface.BOLD)

            instructions.text = r.instructions+"\n"
            instructions.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15F)
            instructions.setTextColor(Color.WHITE)

            //linearLayout.addView(mealThumb)
            linearLayout.addView(mealName)
            linearLayout.addView(ing)
            linearLayout.addView(ingMrs)
            linearLayout.addView(inst)
            linearLayout.addView(instructions)

        }

    }
}







