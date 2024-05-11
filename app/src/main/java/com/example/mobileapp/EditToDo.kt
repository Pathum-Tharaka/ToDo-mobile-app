package com.example.mobileapp


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditToDo : AppCompatActivity() {

    private lateinit var title: EditText
    private lateinit var des: EditText
    private lateinit var edit: Button
    private lateinit var dbHandler: DbHandler
    private lateinit var context: Context
    private var updateDate: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_to_do)

        context = this
        dbHandler = DbHandler(context)

        title = findViewById(R.id.editToDoTextTitle)
        des = findViewById(R.id.editToDoTextDescription)
        edit = findViewById(R.id.buttonEdit)

        val id = intent.getStringExtra("id")
        val todo = dbHandler.getSingleToDo(id?.toInt() ?: 0)

        title.setText(todo?.title)
        des.setText(todo?.description)

        edit.setOnClickListener {
            val titleText = title.text.toString()
            val decText = des.text.toString()
            updateDate = System.currentTimeMillis()

            val toDo = ToDo(id?.toInt() ?: 0, titleText, decText, updateDate, 0)
            val state = dbHandler.updateSingleToDo(toDo)
            println(state)
            startActivity(Intent(context, MainActivity::class.java))
        }
    }
}