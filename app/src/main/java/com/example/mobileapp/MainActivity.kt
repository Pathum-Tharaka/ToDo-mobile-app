package com.example.mobileapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private lateinit var add: Button
    private lateinit var listView: ListView
    private lateinit var count: TextView
    private lateinit var context: Context
    private lateinit var dbHandler: DbHandler
    private lateinit var toDos: MutableList<ToDo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

        dbHandler = DbHandler(context)
        add = findViewById(R.id.add)
        listView = findViewById(R.id.todolist)
        count = findViewById(R.id.todocount)
        toDos = mutableListOf()

        toDos = dbHandler.getAllToDos().toMutableList()

        val adapter = ToDoAdapter(context, R.layout.single_todo, toDos)
        listView.adapter = adapter

        // Get todo counts from the table
        val countTodo = dbHandler.countToDo()
        count.text = "You have $countTodo works"

        add.setOnClickListener {
            startActivity(Intent(context, AddToDo::class.java))
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val todo = toDos[position]
            val builder = AlertDialog.Builder(context)
            builder.setTitle(todo.title)
            builder.setMessage(todo.description)
            builder.setPositiveButton("Finished") { _, _ ->
                todo.finished = System.currentTimeMillis()
                dbHandler.updateSingleToDo(todo)
                startActivity(Intent(context, MainActivity::class.java))
            }
            builder.setNegativeButton("Delete") { _, _ ->
                dbHandler.deleteToDo(todo.id)
                startActivity(Intent(context, MainActivity::class.java))
            }
            builder.setNeutralButton("Update") { _, _ ->
                val intent = Intent(context, EditToDo::class.java)
                intent.putExtra("id", todo.id.toString())
                startActivity(intent)
            }
            builder.show()
        }
    }
}