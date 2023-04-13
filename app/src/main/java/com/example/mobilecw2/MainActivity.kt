package com.example.mobilecw2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//Cut out q: where will be the database stored locally

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "mydatabase").build()
        val recipeDao = db.recipeDao()
        val searchMeals = findViewById<Button>(R.id.mealSearch)
        searchMeals.setOnClickListener {
            val contactIntent = Intent(this, SearchMeal::class.java)
            startActivity(contactIntent)
        }
        val saveBtn = findViewById<Button>(R.id.add)
        saveBtn.setOnClickListener {
            runBlocking {
                launch {

                    val recipe1 = Recipe(7, "7th one", null,
                        "Pork","Chinese",
                        "Preparation\r\n1. Crack the egg into a bowl. Separate the egg white and yolk.\r\n\r\nSweet and Sour Pork\r\n2. Slice the pork tenderloin into ips.\r\n\r\n3. Prepare the marinade using a pinch of salt, one teaspoon of starch, two teaspoons of light soy sauce, and an egg white.\r\n\r\n4. Marinade the pork ips for about 20 minutes.\r\n\r\n5. Put the remaining starch in a bowl. Add some water and vinegar to make a starchy sauce.\r\n\r\nSweet and Sour Pork\r\nCooking Inuctions\r\n1. Pour the cooking oil into a wok and heat to 190\u00b0C (375\u00b0F). Add the marinated pork ips and fry them until they turn brown. Remove the cooked pork from the wok and place on a plate.\r\n\r\n2. Leave some oil in the wok. Put the tomato sauce and white sugar into the wok, and heat until the oil and sauce are fully combined.\r\n\r\n3. Add some water to the wok and thoroughly heat the sweet and sour sauce before adding the pork ips to it.\r\n\r\n4. Pour in the starchy sauce. Stir-fry all the ingredients until the pork and sauce are thoroughly mixed together.\r\n\r\n5. Serve on a plate and add some coriander for decoration.",
                        "https://www.themealdb.com/images/media/meals/1529442316.jpg",
                        "Sweet",
                        "https://www.youtube.com/watch?v=mdaBIhgEAMo",
                        ingredients =listOf("Pork", "Egg", "Water", "Salt", "Sugar", "Soy Sauce", "Starch", "Tomato Puree", "Vinegar", "Coriander"),
                        measures = listOf("200g", "1", "Dash", "1/2 tsp", "1 tsp ", "10g", "10g", "30g", "10g", "Dash"),
                        null,null,null,null)


                    recipeDao.insertRecipe(recipe1)
                    }
                }
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
        }
    }
