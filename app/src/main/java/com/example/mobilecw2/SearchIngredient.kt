package com.example.mobilecw2

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
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
        supportActionBar?.hide()
        setContentView(R.layout.activity_searchingredient)
        val ct = CoroutineTasks()
        val retrieveBtn = findViewById<Button>(R.id.retrieveBtn)
        val save = findViewById<ImageButton>(R.id.saveMeals)

        val scrollView1 = findViewById<ScrollView>(R.id.sv1)
        // create a new LinearLayout with vertical orientation
        val linearLayout1 = LinearLayout(this)
        linearLayout1.orientation = LinearLayout.VERTICAL
        // collecting all the JSON string

        val stb = StringBuilder() // for meal id
        val stb1 = StringBuilder()// for meals.
        val stb2 = StringBuilder()
        val temp = StringBuilder()
        var mealsID = mutableListOf<Int>()


        retrieveBtn.setOnClickListener {

            linearLayout1.removeAllViews()
            scrollView1.removeAllViews()
            stb.clear()
            stb2.clear()
            stb2.append("{ \"meals\":[")
            val input_text = findViewById<EditText>(R.id.input).text.toString()
            mealsID.clear()
            if (input_text.isEmpty()) {
                Toast.makeText(this,"Type something to search", Toast.LENGTH_SHORT ).show()
            } else {
                val progressBar = findViewById<ProgressBar>(R.id.progressBar)
                progressBar.visibility = View.VISIBLE // show the progress bar
                GlobalScope.launch(Dispatchers.Main) {
                    try{
                    withContext(Dispatchers.IO) {

                        val url_string =
                            "https://www.themealdb.com/api/json/v1/1/filter.php?i=$input_text"
                        val url = URL(url_string)
                        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                        val bf = BufferedReader(InputStreamReader(con.inputStream))
                        var line: String? = bf.readLine()

                        while (line != null) {
                            stb.append(line + "\n")
                            line = bf.readLine()
                        }

                        mealsRetrieve(stb,mealsID)

                        for (i in 0..mealsID.size - 1) {
                            stb1.clear()
                            temp.clear()
                            var url_string2 =
                                "https://www.themealdb.com/api/json/v1/1/lookup.php?i=${mealsID[i]}"
                            val url = URL(url_string2)
                            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                            val bf = BufferedReader(InputStreamReader(con.inputStream))
                            var line: String? = bf.readLine()
                            while (line != null) {
                                stb1.append(line + "\n")
                                temp.append(line + "\n")
                                line = bf.readLine()
                            }
                            temp.delete(0, 10)

                            temp.setLength(temp.length - 3)

                            temp.append(",")
                            stb2.append(temp)

                        }
                        stb2.deleteCharAt(stb2.length - 1)
                        stb2.append("]}")

                        ct.mealsView(stb2, linearLayout1,this@SearchIngredient)
                    }
                    progressBar.visibility = View.GONE
                    scrollView1.addView(linearLayout1)

                }catch (e: JSONException){
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@SearchIngredient,"Couldn't find related recipes", Toast.LENGTH_SHORT).show()
                        //coroutineContext.cancel()
                    }
                }

            }


            save.setOnClickListener {

                GlobalScope.launch(Dispatchers.IO) {
                    ct.saveMeal(stb2,this@SearchIngredient)

                }
                Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun mealsRetrieve(stb: java.lang.StringBuilder, mealsID: MutableList<Int>) {
        val json = JSONObject(stb.toString())

        var jsonArray: JSONArray = json.getJSONArray("meals")

        for (i in 0 until jsonArray.length()) {
            val meal: JSONObject = jsonArray[i] as JSONObject // this is a json object
            val mealID = meal["idMeal"] as String
            mealsID.add(mealID.toInt())
        }
    }




}