package com.example.mobileapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

// AddToDo class for adding a new ToDo item
class AddToDo : AppCompatActivity() {

    // UI elements and variables declaration
    private lateinit var title: EditText
    private lateinit var desc: EditText
    private lateinit var add: Button
    private lateinit var dbHandler: DbHandler
    private lateinit var context: Context

    // Function called when activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_do)

        // Initialize UI elements and context
        title = findViewById(R.id.editTextTitle)
        desc = findViewById(R.id.editTextDescription)
        add = findViewById(R.id.buttonAdd)
        context = this
        dbHandler = DbHandler(context)

        // OnClickListener for add button to add a new ToDo item
        add.setOnClickListener {
            val userTitle = title.text.toString()
            val userDesc = desc.text.toString()
            val started = System.currentTimeMillis()
            val toDo = ToDo(userTitle, userDesc, started, 0) // Create new ToDo object
            dbHandler.addToDo(toDo) // Add ToDo item to the database
            startActivity(Intent(context, MainActivity::class.java)) // Navigate back to MainActivity
        }
    }
}
