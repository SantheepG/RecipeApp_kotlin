package com.example.mobilecw2

import android.graphics.*
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.*
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class SearchMeal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchmeal)

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
                runBlocking {
                    withContext(Dispatchers.IO) {
                        val recipes: List<Recipe> = recipeDao.getRecipe(input)
                        updateUI(recipes,linearLayout)
                    }
                }
            }
            scrollView.addView(linearLayout)
        }
    }


    suspend fun updateUI(recipe: List<Recipe>, linearLayout: LinearLayout){
        var count = 0
        for(r in recipe){
            count++
            val mealThumb = ImageView(this)
            val mealName = TextView(this)
            val desc = TextView(this)
            val ing = TextView(this)
            val ingMrs = TextView(this)
            val inst = TextView(this)
            val instructions = TextView(this)
            val divider = TextView(this)

            var urlString = "${r.mealThumb}"
            val url = URL(urlString)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            val inputStream: InputStream = con.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Set the maximum height of the image view to 200 pixels
            val layoutParams = ViewGroup.LayoutParams(400, 400)
            mealThumb.layoutParams = layoutParams
            // create a circular bitmap with the same width and height as the original bitmap
            val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)
            val radius = bitmap.width / 2f
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 100, 88)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(radius, radius, radius, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)
            mealThumb.setImageBitmap(output)


            mealName.text = "\n${count}. ${r.meal}\n"
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

            divider.text = "\t"

            
            linearLayout.addView(mealName)
            linearLayout.addView(mealThumb)
            linearLayout.addView(ing)
            linearLayout.addView(ingMrs)
            linearLayout.addView(inst)
            linearLayout.addView(instructions)

        }

    }
}







