package com.example.mobileapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditToDo : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var editButton: Button
    private lateinit var dbHandler: DbHandler
    private lateinit var context: Context
    private var todoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_to_do)

        context = this
        dbHandler = DbHandler(context)

        titleEditText = findViewById(R.id.editToDoTextTitle)
        descriptionEditText = findViewById(R.id.editToDoTextDescription)
        editButton = findViewById(R.id.buttonEdit)

        val id = intent.getStringExtra("id")
        val todo = id?.let { dbHandler.getSingleToDo(it.toInt()) }

        if (todo != null) {
            todoId = todo.id
            titleEditText.setText(todo.title)
            descriptionEditText.setText(todo.description)
        } else {
            showToast("Invalid todo id")
            finish() // Finish the activity if the todo id is invalid
        }

        editButton.setOnClickListener {
            val titleText = titleEditText.text.toString()
            val descriptionText = descriptionEditText.text.toString()
            val updateDate = System.currentTimeMillis()

            val updatedTodo = ToDo(todoId, titleText, descriptionText, updateDate, 0)
            val updateStatus = dbHandler.updateSingleToDo(updatedTodo)

            if (updateStatus > 0) {
                showToast("Todo updated successfully")
                startActivity(Intent(context, MainActivity::class.java))
            } else {
                showToast("Failed to update todo")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}