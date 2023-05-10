package com.example.mobilecw2

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchWeb:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchweb)

        val inputTxt = findViewById<EditText>(R.id.inputTxt)
        val searchBtn = findViewById<Button>(R.id.searchBtn)

        val scrollView1 = findViewById<ScrollView>(R.id.scrollView)
        // create a new LinearLayout with vertical orientation
        val linearLayout1 = LinearLayout(this)
        linearLayout1.orientation = LinearLayout.VERTICAL
        // collecting all the JSON string

        val stb = StringBuilder() // for meal


        searchBtn.setOnClickListener {
            linearLayout1.removeAllViews()
            scrollView1.removeAllViews()
            stb.clear()

            GlobalScope.launch (Dispatchers.Main){
                val proBar = findViewById<ProgressBar>(R.id.proBar)
                proBar.visibility = View.VISIBLE
                withContext(Dispatchers.IO){

                    val input_text = findViewById<EditText>(R.id.inputTxt).text.toString()
                    val url_string = "https://www.themealdb.com/api/json/v1/1/search.php?s=$input_text"
                    val url = URL(url_string)
                    val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                    val bf = BufferedReader(InputStreamReader(con.inputStream))
                    var line: String? = bf.readLine()
                    while (line != null) {
                        stb.append(line + "\n")
                        line = bf.readLine()
                    }
                    mealsView(stb,linearLayout1)
                }
                proBar.visibility = View.GONE
                scrollView1.addView(linearLayout1)

            }

        }

    }

    suspend fun mealsView(stb2: java.lang.StringBuilder,linearLayout1: LinearLayout){
        val json = JSONObject(stb2.toString())
        val jsonArray: JSONArray = json.getJSONArray("meals")

        for (i in 0..jsonArray.length()-1) {
            val meal: JSONObject = jsonArray[i] as JSONObject // this is a json object
            val idMeal = meal["idMeal"].toString().toInt()
            val strMeal = meal["strMeal"].toString()
            val drinkAlt = meal["strDrinkAlternate"].toString()
            val category = meal["strCategory"].toString()
            val area = meal["strArea"].toString()
            val instructions = meal["strInstructions"].toString()
            val mealThumb = meal["strMealThumb"].toString()
            val tags = meal["strTags"].toString()
            val youtube = meal["strYoutube"].toString()
            val ingredients = mutableListOf<String>()
            val measures = mutableListOf<String>()
            //ingredients.add(jsonObject.toString())
            val keysIterator = meal.keys()
            while (keysIterator.hasNext()) {
                val key = keysIterator.next()
                //ingredients.add(key)
                if (key.startsWith("strIngredient")) {
                    ingredients.add(meal.getString(key))
                } else if (key.startsWith("strMeasure")) {
                    measures.add(meal.getString(key))
                }
            }
            val src = meal["strSource"].toString()
            val imgSrc = meal["strImageSource"].toString()
            val creativeCommonsConfirmed = meal["strCreativeCommonsConfirmed"].toString()
            val dateModified = meal["dateModified"].toString()

            val mealName = TextView(this)
            val desc = TextView(this)
            val ing = TextView(this)
            val ingMrs = TextView(this)
            val instTitle = TextView(this)
            val inst = TextView(this)
            mealName.text = "${i+1}. $strMeal"
            mealName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20F)
            mealName.setTextColor(Color.WHITE)
            mealName.setTypeface(null, Typeface.BOLD)

            desc.text = "Drink alternate: $drinkAlt\nCategory: $category\nArea: $area\n\n"
            desc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15F)
            desc.setTextColor(Color.WHITE)

            ing.text = "\nIngredients & Measures"
            ing.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15F)
            ing.setTextColor(Color.WHITE)
            ing.setTypeface(null, Typeface.BOLD)

            for (i in 0 until (ingredients?.size ?: 0)) {
                ingMrs.append("${ingredients?.get(i)}-${measures?.get(i)}, ")
            }
            ingMrs.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15F)
            ingMrs.setTextColor(Color.WHITE)

            instTitle.text = "\nInstructions"
            instTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15F)
            instTitle.setTextColor(Color.WHITE)
            instTitle.setTypeface(null, Typeface.BOLD)

            inst.text = "$instructions\n"
            inst.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15F)
            inst.setTextColor(Color.WHITE)

            //linearLayout.addView(mealThumb)
            linearLayout1.addView(mealName)
            linearLayout1.addView(ing)
            linearLayout1.addView(ingMrs)
            linearLayout1.addView(instTitle)
            linearLayout1.addView(inst)
        }
    }
}