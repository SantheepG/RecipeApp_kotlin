package com.example.mobilecw2

import android.os.Bundle
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
        val tv = findViewById<TextView>(R.id.tv)
        // collecting all the JSON string


        //var url_string = "https://www.themealdb.com/api/json/v1/1/filter.php?i="
        val stb = StringBuilder() // for meal id
        val stb1 = StringBuilder()// for meals.
        val stb2 = StringBuilder()
        val temp = StringBuilder()
        var mealsID = mutableListOf<Int>()
        //var con: HttpURLConnection = url.openConnection() as HttpURLConnection
        retrieveBtn.setOnClickListener {
            stb.clear()
            stb2.clear()
            stb2.append("{ \"meals\":[")
            val allMeals = StringBuilder()
            mealsID.clear()
            //allMeals.clear()
            GlobalScope.launch(Dispatchers.IO) {
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

                parseJSON(stb, mealsID, tv)

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
                //parseMeal(stb2,allMeals,tv)
                //tv.text = stb1.toString()

                runOnUiThread {
                    //val json = JSONObject(stb2.toString())
                    //val allMeals = java.lang.StringBuilder()
                    //val jsonArray: JSONArray = json.getJSONArray("allMeals")
                    tv.text = stb2.toString()
                    //Stuff that updates the UI
                }
                //runOnUiThread {
                 //   tv.text = stb1.toString()
                    //Stuff that updates the UI
                //}
            }
            Toast.makeText(this, "Type something to search", Toast.LENGTH_SHORT).show()
        }
        save.setOnClickListener {
            if(stb1.isNotEmpty()){
                GlobalScope.launch(Dispatchers.IO){
                    saveMeal(stb2,tv)
                }
            }
        }
    }


    suspend fun parseJSON(stb: java.lang.StringBuilder, mealsID: MutableList<Int>, tv: TextView) {
        val json = JSONObject(stb.toString())

        var jsonArray: JSONArray = json.getJSONArray("meals")

        for (i in 0 until jsonArray.length()) {
            val meal: JSONObject = jsonArray[i] as JSONObject // this is a json object
            val mealID = meal["idMeal"] as String
            mealsID.add(mealID.toInt())
        }
    }

    suspend fun parseMeal(stb1: java.lang.StringBuilder,allMeals: java.lang.StringBuilder,tv:TextView){
        val json = JSONObject(stb1.toString())
        //val allMeals = java.lang.StringBuilder()
        val jsonArray: JSONArray = json.getJSONArray("meals")
        //val jsonObject = jsonArray.getJSONObject(0)
        for (i in 0..jsonArray.length()-1) {
            val meal: JSONObject = jsonArray[i] as JSONObject // this is a json object
            val idMeal = meal["idMeal"]
            val strMeal = meal["strMeal"]
            val drinkAlt = meal["strDrinkAlternate"]
            val category = meal["strCategory"]
            val area = meal["strArea"]
            val instructions = meal["strInstructions"]
            val mealThumb = meal["strMealThumb"]
            val tags = meal["strTags"]
            val youtube = meal["strYoutube"]
            val ingredients = mutableListOf<String>()
            val measures = mutableListOf<String>()
            //ingredients.add(jsonObject.toString())
            val keysIterator = meal.keys()
            while (keysIterator.hasNext()){
                val key = keysIterator.next()
                //ingredients.add(key)
                if (key.startsWith("strIngredient")){
                    ingredients.add(meal.getString(key))
                }
                else if(key.startsWith("strMeasure")){
                    measures.add(meal.getString(key))
                }
            }


            //for (i in 0 until jsonObject.length()){
            //    if (keysIterator != null) {
            //        if (keysIterator.startsWith("Ingredient")){
            //            ingredients.add(keysIterator)
            //        }
            //    }
            //}
            //for (i in 0 until jsonObject.length()){
                //ingredients.add(jsonObject.toString())
            //    ingredients.add(jsonObject.getString("ingredient1"))
                //if(jsonObject.toString().startsWith("Ingredient")){
            //        ingredients.add(jsonObject.getString("ingredient1"))
                //}
                //if (mealObject.getString().startsWith("Ingredient")){
                //    ingredients.add(mealObject.getString())
                //}
            //}
            //while (keysIterator.hasNext()) {
             //   var key = keysIterator.next();
            //     if (key.startsWith("Ingredient")) {ingredients.add(key) };
             //   }

            //val ing1 = meal["strIngredient1"]
            allMeals.append("\"$strMeal\" \n\t\t\t\t \"$ingredients \n\t\t\t\t \"$measures\n")
            //allMeals.append("${i+1}). \"$strMeal\"\n\t \"$drinkAlt\"\n\t \"$category\"\n\t \"$area\"\n\t \"$instructions\"\n\n")
        }

    }

    suspend fun saveMeal(stb2: java.lang.StringBuilder, tv: TextView){
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "DB8").build()
        val recipeDao = db.recipeDao()
        val json = JSONObject(stb2.toString())
        val jsonArray: JSONArray = json.getJSONArray("meals")
        //val jsonObject = jsonArray.getJSONObject(0)
        for (i in 0..jsonArray.length()-1) {
            val meal: JSONObject = jsonArray[i] as JSONObject // this is a json object
            val idMeal = meal["idMeal"]
            val strMeal = meal["strMeal"]
            val drinkAlt = meal["strDrinkAlternate"]
            val category = meal["strCategory"]
            val area = meal["strArea"]
            val instructions = meal["strInstructions"]
            val mealThumb = meal["strMealThumb"]
            val tags = meal["strTags"]
            val youtube = meal["strYoutube"]
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
        }

    }

}