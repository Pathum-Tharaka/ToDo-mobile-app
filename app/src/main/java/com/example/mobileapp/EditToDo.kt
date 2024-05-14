package com.example.mobileapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// EditToDo class responsible for editing a to-do item
class EditToDo : AppCompatActivity() {

    // UI elements and variables declaration
    private lateinit var titleEditText: EditText
    private lateinit var desEditText: EditText
    private lateinit var editButton: Button
    private lateinit var dbHandler: DbHandler
    private lateinit var context: Context
    private var updateDate: Long = 0



    // Function called when activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_to_do)

        // Initialize context and database handler
        context = this
        dbHandler = DbHandler(context)

        // Initialize UI elements
        titleEditText = findViewById(R.id.editToDoTextTitle)
        desEditText = findViewById(R.id.editToDoTextDescription)
        editButton = findViewById(R.id.buttonEdit)

        // Retrieve to-do ID from intent and fetch corresponding to-do from database
        val id = intent.getStringExtra("id")
        val todo = id?.toInt()?.let { dbHandler.getSingleToDo(it) }

        // If to-do exists, populate UI elements with its data
        todo?.let { toDo ->
            titleEditText.setText(toDo.title)
            desEditText.setText(toDo.description)

            // OnClickListener for editButton to update to-do item
            editButton.setOnClickListener {
                val titleText = titleEditText.text.toString()
                val descText = desEditText.text.toString()
                updateDate = System.currentTimeMillis()

                // Create updated ToDo object with new data
                val updatedToDo = ToDo(
                    id = toDo.id,
                    title = titleText,
                    description = descText,
                    started = toDo.started, // Access started property directly
                    finished = updateDate
                )

                // Update to-do item in the database
                val status = dbHandler.updateSingleToDo(updatedToDo)

                // If update successful, navigate back to MainActivity
                if (status > 0) {
                    startActivity(Intent(context, MainActivity::class.java))
                } else {
                    // Show toast message if update fails
                    Toast.makeText(context, "Failed to update to-do item", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
