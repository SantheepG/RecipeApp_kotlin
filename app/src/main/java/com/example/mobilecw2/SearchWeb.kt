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
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchWeb:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_searchweb)

        val ct = CoroutineTasks()

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

            val SearchIngredient = SearchIngredient()
            GlobalScope.launch (Dispatchers.Main){
                val progressBar = findViewById<ProgressBar>(R.id.proBar)
                progressBar.visibility = View.VISIBLE
                try {
                    withContext(Dispatchers.IO) {

                        val input_text = findViewById<EditText>(R.id.inputTxt).text.toString()
                        val url_string =
                            "https://www.themealdb.com/api/json/v1/1/search.php?s=$input_text"
                        val url = URL(url_string)
                        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                        val bf = BufferedReader(InputStreamReader(con.inputStream))
                        var line: String? = bf.readLine()
                        while (line != null) {
                            stb.append(line + "\n")
                            line = bf.readLine()
                        }
                        ct.mealsView(stb, linearLayout1, this@SearchWeb)
                    }
                    progressBar.visibility = View.GONE
                    scrollView1.addView(linearLayout1)
                }catch (e: JSONException){
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@SearchWeb,"Couldn't find related recipes", Toast.LENGTH_SHORT).show()
                    //coroutineContext.cancel()
                }
            }

        }

    }

}