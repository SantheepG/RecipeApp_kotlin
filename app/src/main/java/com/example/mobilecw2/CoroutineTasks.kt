package com.example.mobilecw2

import android.app.Activity
import android.graphics.*
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat.getColor
import androidx.core.content.ContextCompat
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class CoroutineTasks {
    suspend fun updateUI(recipe: List<Recipe>, linearLayout: LinearLayout, activity: Activity){
        var count = 0
        for(r in recipe){
            count++
            val mealThumb = ImageView(activity)
            val mealName = TextView(activity)
            val desc = TextView(activity)
            val ing = TextView(activity)
            val ingMrs = TextView(activity)
            val inst = TextView(activity)
            val instructions = TextView(activity)
            val divider = TextView(activity)

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

            // Create a new CardView instance
            val cardView = CardView(activity)
            val color = ContextCompat.getColor(activity, R.color.green)
            cardView.setCardBackgroundColor(color)

// Set the layout parameters for the CardView
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(16, 10, 16, 16)
            cardView.layoutParams = params

// Set the CardView properties
            cardView.radius = 30f
            cardView.cardElevation = 20f
            cardView.setContentPadding(16, 10, 16, 16)


            // Create a new LinearLayout instance
            val linearLayout1 = LinearLayout(activity)

// Set the orientation of the LinearLayout
            linearLayout1.orientation = LinearLayout.VERTICAL

// Set the layout parameters for the LinearLayout

            linearLayout1.layoutParams = params

            withContext(Dispatchers.Main) {

                linearLayout1.addView(mealName)
                linearLayout1.addView(mealThumb)
                linearLayout1.addView(ing)
                linearLayout1.addView(ingMrs)
                linearLayout1.addView(inst)
                linearLayout1.addView(instructions)
                cardView.addView(linearLayout1)
                linearLayout.addView(cardView)
            }

        }

    }
    fun mealsView(stb2: java.lang.StringBuilder, linearLayout1: LinearLayout, activity: Activity){
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

            val mealName = TextView(activity)
            val desc = TextView(activity)
            val ing = TextView(activity)
            val ingMrs = TextView(activity)
            val instTitle = TextView(activity)
            val inst = TextView(activity)
            mealName.text = "\n${i+1}. $strMeal"
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

            // Create a new CardView instance
            val cardView = CardView(activity)
            val color = ContextCompat.getColor(activity, R.color.green)
            cardView.setCardBackgroundColor(color)

// Set the layout parameters for the CardView
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(16, 10, 16, 16)
            cardView.layoutParams = params

// Set the CardView properties
            cardView.radius = 30f
            cardView.cardElevation = 20f
            cardView.setContentPadding(16, 10, 16, 16)


            // Create a new LinearLayout instance
            val linearLayout = LinearLayout(activity)

// Set the orientation of the LinearLayout
            linearLayout.orientation = LinearLayout.VERTICAL

// Set the layout parameters for the LinearLayout

            linearLayout.layoutParams = params
            linearLayout.addView(mealName)
            linearLayout.addView(ing)
            linearLayout.addView(ingMrs)
            linearLayout.addView(instTitle)
            linearLayout.addView(inst)
            cardView.addView(linearLayout)
            linearLayout1.addView(cardView)

        }
    }
    suspend fun saveMeal(stb2: java.lang.StringBuilder, activity: Activity){
        val db = Room.databaseBuilder(activity, AppDatabase::class.java, "DB10").build()
        val recipeDao = db.recipeDao()
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
            val keysIterator = meal.keys()
            while (keysIterator.hasNext()) {
                val key = keysIterator.next()
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

            val recipe = Recipe(id = idMeal,meal=strMeal, drinkAlternate = drinkAlt,category=category,
                area = area, instructions = instructions,mealThumb=mealThumb,tags=tags, youtube = youtube,ingredients=ingredients,
                measures = measures, src = src, imgSrc = imgSrc, CreativeCommonsConfirmed = creativeCommonsConfirmed, dateModified = dateModified)
            recipeDao.insertRecipe(recipe)
        }

    }
}