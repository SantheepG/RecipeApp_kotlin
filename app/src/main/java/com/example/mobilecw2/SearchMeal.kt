package com.example.mobilecw2

import android.app.Activity
import android.graphics.*
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.room.Room
import kotlinx.coroutines.*
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class SearchMeal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_searchmeal)
        val ct = CoroutineTasks()
        // assuming you have a reference to your ScrollView
        val scrollView = findViewById<ScrollView>(R.id.scrollView)

        // create a new LinearLayout with vertical orientation
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL

        val db = Room.databaseBuilder(this, AppDatabase::class.java, "DB10").build()
        val recipeDao = db.recipeDao()
        val search = findViewById<Button>(R.id.searchButton)
        search.setOnClickListener {

            linearLayout.removeAllViews()
            scrollView.removeAllViews()
            val input = findViewById<EditText>(R.id.inputText).text.toString()
            if (input != ""){
 // show the progress bar
                GlobalScope.launch(Dispatchers.Main) {
                    val proBar = findViewById<ProgressBar>(R.id.proBar)
                    proBar.visibility = View.VISIBLE
                    withContext(Dispatchers.IO) {
                        val recipes: List<Recipe> = recipeDao.getRecipe(input)
                        ct.updateUI(recipes,linearLayout,this@SearchMeal)
                    }
                    proBar.visibility = View.GONE
                    scrollView.addView(linearLayout)
                }

            }
            else{
                Toast.makeText(this,"Field is empty",Toast.LENGTH_SHORT).show()
            }
        }
    }
}







