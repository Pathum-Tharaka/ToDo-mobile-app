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

// MainActivity class responsible for the main activity of the app
class MainActivity : AppCompatActivity() {

    // UI elements and variables declaration
    private lateinit var add: Button
    private lateinit var listView: ListView
    private lateinit var count: TextView
    private lateinit var context: Context
    private lateinit var dbHandler: DbHandler
    private lateinit var toDos: MutableList<ToDo>
    private lateinit var searchEditText: EditText
    private lateinit var adapter: ToDoAdapter

    // Function called when activity is created
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

        // Initialize database handler and UI elements
        dbHandler = DbHandler(context)
        add = findViewById(R.id.add)
        listView = findViewById(R.id.todolist)
        count = findViewById(R.id.todocount)
        searchEditText = findViewById(R.id.searchEditText)
        toDos = mutableListOf()

        // Retrieve all todos from the database
        toDos = dbHandler.getAllToDos().toMutableList()

        // Initialize adapter and set it to the list view
        adapter = ToDoAdapter(context, R.layout.single_todo, toDos)
        listView.adapter = adapter

        // Get and display total todo count
        val countTodo = dbHandler.countToDo()
        count.text = "You have $countTodo works"

        // OnClickListener for add button to navigate to AddToDo activity
        add.setOnClickListener {
            startActivity(Intent(context, AddToDo::class.java))
        }

        // OnClickListener for list view items to display options when clicked
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val todo = toDos[position]
            val builder = AlertDialog.Builder(context)
            builder.setTitle(todo.title)
            builder.setMessage(todo.description)
            builder.setPositiveButton("Finished") { _, _ ->
                // Mark todo as finished and update database
                todo.finished = System.currentTimeMillis()
                dbHandler.updateSingleToDo(todo)
                startActivity(Intent(context, MainActivity::class.java))
            }
            builder.setNegativeButton("Delete") { _, _ ->
                // Confirm deletion of todo
                val alert = AlertDialog.Builder(this)
                alert.setMessage("Are you sure you wanna delete this note?")
                alert.setCancelable(false)
                alert.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                    Log.d("Cancel", "Cancelled deletion")
                })
                alert.setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                    // Delete todo and refresh list
                    dbHandler.deleteToDo(todo.id)
                    startActivity(Intent(context, MainActivity::class.java))
                })
                alert.show()
            }
            builder.setNeutralButton("Update") { _, _ ->
                // Navigate to EditToDo activity with todo ID
                val intent = Intent(context, EditToDo::class.java)
                intent.putExtra("id", todo.id.toString())
                startActivity(intent)
            }
            builder.show()
        }

        // TextWatcher for searchEditText to filter list view based on input text
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
