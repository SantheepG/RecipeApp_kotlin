package com.example.mobilecw2

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Dictionary
import java.util.Objects

class SearchIngredient: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchingredient)

        val retrieveBtn = findViewById<Button>(R.id.retrieveBtn)
        val save = findViewById<ImageButton>(R.id.saveMeals)

        val scrollView1 = findViewById<ScrollView>(R.id.sv1)
        // create a new LinearLayout with vertical orientation
        val linearLayout1 = LinearLayout(this)
        linearLayout1.orientation = LinearLayout.VERTICAL
        // collecting all the JSON string


        //var url_string = "https://www.themealdb.com/api/json/v1/1/filter.php?i="
        val stb = StringBuilder() // for meal id
        val stb1 = StringBuilder()// for meals.
        val stb2 = StringBuilder()
        val temp = StringBuilder()
        var mealsID = mutableListOf<Int>()

        val meals = StringBuilder()
        //var con: HttpURLConnection = url.openConnection() as HttpURLConnection
        retrieveBtn.setOnClickListener {
            linearLayout1.removeAllViews()
            scrollView1.removeAllViews()
            stb.clear()
            stb2.clear()
            stb2.append("{ \"meals\":[")
            val allMeals = StringBuilder()
            mealsID.clear()
            //allMeals.clear()
            runBlocking {
                withContext(Dispatchers.IO){
                    val input_text = findViewById<EditText>(R.id.input).text.toString()
                    val url_string = "https://www.themealdb.com/api/json/v1/1/filter.php?i=$input_text"
                    val url = URL(url_string)
                    val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                    val bf = BufferedReader(InputStreamReader(con.inputStream))
                    var line: String? = bf.readLine()

                    while (line != null) {
                        stb.append(line + "\n")
                        line = bf.readLine()
                    }

                    parseJSON(stb, mealsID)

                    for (i in 0..mealsID.size - 1) {
                        stb1.clear()
                        temp.clear()
                        var url_string2 = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=${mealsID[i]}"
                        val url = URL(url_string2)
                        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                        val bf = BufferedReader(InputStreamReader(con.inputStream))
                        var line: String? = bf.readLine()
                        while (line != null) {
                            stb1.append(line + "\n")
                            temp.append(line + "\n")
                            //stb2.append(line + "\n")
                            line = bf.readLine()
                        }
                        temp.delete(0,10)
                        //temp.deleteCharAt(temp.length)
                        temp.deleteCharAt(temp.length-1)
                        temp.deleteCharAt(temp.length-1)
                        temp.deleteCharAt(temp.length-1)
                        //temp.deleteCharAt(temp.length-4)
                        temp.append(",")
                        stb2.append(temp)
                        //parseMeal(stb1,allMeals,tv)
                    }
                    stb2.deleteCharAt(stb2.length -1)
                    stb2.append("]}")

                    mealsView(stb2,linearLayout1)
                }
            }
            scrollView1.addView(linearLayout1)
        }

        save.setOnClickListener {
            if(stb1.isNotEmpty()){
                GlobalScope.launch(Dispatchers.IO){
                    saveMeal(stb2)
                }
            }
            Toast.makeText(this, "Succes", Toast.LENGTH_SHORT).show()
        }
    }


    suspend fun parseJSON(stb: java.lang.StringBuilder, mealsID: MutableList<Int>) {
        val json = JSONObject(stb.toString())

        var jsonArray: JSONArray = json.getJSONArray("meals")

        for (i in 0 until jsonArray.length()) {
            val meal: JSONObject = jsonArray[i] as JSONObject // this is a json object
            val mealID = meal["idMeal"] as String
            mealsID.add(mealID.toInt())
        }
    }

    suspend fun mealsView(stb2: java.lang.StringBuilder,linearLayout1: LinearLayout){
        val json = JSONObject(stb2.toString())
        val jsonArray: JSONArray = json.getJSONArray("meals")
        //val jsonObject = jsonArray.getJSONObject(0)
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

    suspend fun saveMeal(stb2: java.lang.StringBuilder){
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "DB10").build()
        val recipeDao = db.recipeDao()
        val json = JSONObject(stb2.toString())

        val jsonArray: JSONArray = json.getJSONArray("meals")
        //val jsonObject = jsonArray.getJSONObject(0)
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

            //meals.append("$idMeal\n$strMeal\n$drinkAlt\n$category\n$area\n$instructions\n$mealThumb\n$tags\n$youtube\n$ingredients\n$measures\n$src\n$imgSrc\n$creativeCommonsConfirmed\n$dateModified\n\n")
            val recipe = Recipe(id = idMeal,meal=strMeal, drinkAlternate = drinkAlt,category=category,
                area = area, instructions = instructions,mealThumb=mealThumb,tags=tags, youtube = youtube,ingredients=ingredients,
                measures = measures, src = src, imgSrc = imgSrc, CreativeCommonsConfirmed = creativeCommonsConfirmed, dateModified = dateModified)
            recipeDao.insertRecipe(recipe)
        }

        //runOnUiThread {
        //    tv.text = meals.toString()
        //}


    }

}