package com.example.mobileapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class AddToDo : AppCompatActivity() {

    private lateinit var title: EditText
    private lateinit var desc: EditText
    private lateinit var add: Button
    private lateinit var dbHandler: DBhandler
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_do)

        title = findViewById(R.id.editTextTitle)
        desc = findViewById(R.id.editTextDescription)
        add = findViewById(R.id.buttonAdd)
        context = this
        dbHandler = DBhandler(context)

        add.setOnClickListener {
            val userTitle = title.text.toString()
            val userDesc = desc.text.toString()
            val started = System.currentTimeMillis()
            val toDo = ToDo(userTitle, userDesc, started, 0)
            dbHandler.addToDo(toDo)
            startActivity(Intent(context, MainActivity::class.java))
        }
    }
}