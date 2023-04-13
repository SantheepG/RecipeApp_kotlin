package com.example.mobilecw2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchIngredient: AppCompatActivity() {
    lateinit var tv:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchingredient)

        val retrieveBtn = findViewById<Button>(R.id.retrieveBtn)
        val save = findViewById<Button>(R.id.saveMeals)

        // collecting all the JSON string
        var stb = StringBuilder()
        var url_string = "https://www.themealdb.com/api/json/v1/1/search.php?s=Arrabita"
        val url = URL(url_string)
        var con: HttpURLConnection = url.openConnection() as HttpURLConnection
        retrieveBtn.setOnClickListener {
            val input = findViewById<EditText>(R.id.input).text.toString()
            url_string += input
            runBlocking {
                launch {
// run the code of the coroutine in a new thread
                    withContext(Dispatchers.IO) {
                        var bf = BufferedReader(InputStreamReader(con.inputStream))
                        var line: String? = bf.readLine()
                        while (line != null) {
                            stb.append(line + "\n")
                            line = bf.readLine()
                        }
                        parseJSON(stb)
                    }
                }
            }
        }

    }
    suspend fun parseJSON(stb: java.lang.StringBuilder) {
// this contains the full JSON returned by the Web Service
        val json = JSONObject(stb.toString())
// Information about all the books extracted by this function
        var allMeals = java.lang.StringBuilder()
        var jsonArray: JSONArray = json.getJSONArray("meals")
// extract all the books from the JSON array
        for (i in 0..jsonArray.length()-1) {
            val meal: JSONObject = jsonArray[i] as JSONObject // this is a json object
// extract the title
            val strMeal = meal["strMeal"] as String
            val strCategory = meal["strCategory"] as String
            allMeals.append("${i+1}. $strMeal - $strCategory ")

            //val volInfo = book["volumeInfo"] as JSONObject
            //val title = volInfo["title"] as String
            //allBooks.append("${i+1}) \"$title\" ")
// extract all the authors
            //val authors = volInfo["authors"] as JSONArray
            //allBooks.append("authors: ")
            //for (i in 0..authors.length()-1)
            //    allBooks.append(authors[i] as String + ", ")
            //allBooks.append("\n\n")
        }
        runOnUiThread {
            tv.text = allMeals
            // Stuff that updates the UI
        }

    }

}