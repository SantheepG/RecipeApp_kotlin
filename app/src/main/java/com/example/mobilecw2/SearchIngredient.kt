package com.example.mobilecw2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchIngredient: AppCompatActivity() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchingredient)

        val retrieveBtn = findViewById<Button>(R.id.retrieveBtn)
        val save = findViewById<Button>(R.id.saveMeals)
        val tv = findViewById<TextView>(R.id.tv)
        // collecting all the JSON string

        //var url_string = "https://www.themealdb.com/api/json/v1/1/filter.php?i="
        val stb = StringBuilder()
        val stb1 = StringBuilder()
        //var con: HttpURLConnection = url.openConnection() as HttpURLConnection
        retrieveBtn.setOnClickListener {
            stb.clear()
            GlobalScope.launch(Dispatchers.IO) {
                val input_text = findViewById<EditText>(R.id.input).text.toString()
                var url_string = "https://www.themealdb.com/api/json/v1/1/filter.php?i=$input_text"
                val url = URL(url_string)
                val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                val bf = BufferedReader(InputStreamReader(con.inputStream))
                var line: String? = bf.readLine()
                //val stb = StringBuilder()
                while (line != null) {
                    stb.append(line + "\n")
                    line = bf.readLine()
                }
                withContext(Dispatchers.Main) {
                    parseJSON(stb,stb1,tv)
                }
            }
            
        }

    }
    suspend fun parseJSON(stb: java.lang.StringBuilder,stb1: java.lang.StringBuilder,tv:TextView) {
        var json = JSONObject(stb.toString())
        var mealsID = mutableListOf<Int>()
        var jsonArray: JSONArray = json.getJSONArray("meals")

        for (i in 0..jsonArray.length()-1) {
            val meal: JSONObject = jsonArray[i] as JSONObject // this is a json object
            val mealID = meal["idMeal"] as String
            mealsID.add(mealID.toInt())
        }
        for(i in 0..mealsID.size-1){
            GlobalScope.launch (Dispatchers.IO) {
                var url_string2 = "www.themealdb.com/api/json/v1/1/lookup.php?i=${mealsID.get(i)}"
                val url = URL(url_string2)
                val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                val bf = BufferedReader(InputStreamReader(con.inputStream))
                var line: String? = bf.readLine()
                while (line != null) {
                    stb1.append(line + "\n")
                    line = bf.readLine()
                }
            }
        }
        //GlobalScope.launch (Dispatchers.IO){
            //var url_string2 = "www.themealdb.com/api/json/v1/1/lookup.php?i=52772$input_text"

        //}
// this contains the full JSON returned by the Web Service

// Information about all the books extracted by this function


// extract all the books from the JSON array


            //val volInfo = book["volumeInfo"] as JSONObject
            //val title = volInfo["title"] as String
            //allBooks.append("${i+1}) \"$title\" ")
// extract all the authors
            //val authors = volInfo["authors"] as JSONArray
            //allBooks.append("authors: ")
            //for (i in 0..authors.length()-1)
            //    allBooks.append(authors[i] as String + ", ")
            //allBooks.append("\n\n")

        runOnUiThread {
            tv.text = mealsID.toString()
            // Stuff that updates the UI
        }

    }

}