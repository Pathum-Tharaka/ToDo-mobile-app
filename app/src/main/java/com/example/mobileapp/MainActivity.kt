package com.example.mobileapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var add: Button
    private lateinit var listView: ListView
    private lateinit var count: TextView
    private lateinit var context: Context
    private lateinit var dbHandler: DbHandler
    private lateinit var toDos: MutableList<ToDo>
    private lateinit var searchEditText: EditText
    private lateinit var adapter: ToDoAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

        dbHandler = DbHandler(context)
        add = findViewById(R.id.add)
        listView = findViewById(R.id.todolist)
        count = findViewById(R.id.todocount)
        searchEditText = findViewById(R.id.searchEditText)
        toDos = mutableListOf()

        toDos = dbHandler.getAllToDos().toMutableList()
        adapter = ToDoAdapter(context, R.layout.single_todo, toDos)
        listView.adapter = adapter

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
                refreshListView()
            }
            builder.setNegativeButton("Delete") { _, _ ->
                val alert = AlertDialog.Builder(this)
                alert.setMessage("Are you sure you wanna delete this note?")
                alert.setCancelable(false)
                alert.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                    Log.d("Cancel", "Cancelled deletion")
                })
                alert.setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                    dbHandler.deleteToDo(todo.id)
                    refreshListView()
                })
                alert.show()
            }
            builder.setNeutralButton("Update") { _, _ ->
                val intent = Intent(context, EditToDo::class.java)
                intent.putExtra("id", todo.id.toString())
                startActivity(intent)
            }
            builder.show()
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun refreshListView() {
        toDos.clear()
        toDos.addAll(dbHandler.getAllToDos())
        adapter.notifyDataSetChanged()
    }
}
