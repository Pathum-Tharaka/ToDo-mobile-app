package com.example.mobileapp
import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.String
import kotlin.Int


class MainActivity : AppCompatActivity() {
    private var add: Button? = null
    private var listView: ListView? = null
    private var count: TextView? = null
    var context: Context? = null
    private var dbHandler: DbHandler? = null
    private var toDos: List<ToDo>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        dbHandler = DbHandler(context)
        add = findViewById<Button>(R.id.add)
        listView = findViewById<ListView>(R.id.todolist)
        count = findViewById<TextView>(R.id.todocount)
        toDos = ArrayList<ToDo>()
        toDos = dbHandler.getAllToDos()
        val adapter = ToDoAdapter(context, R.layout.single_todo, toDos)
        listView.setAdapter(adapter)

        //get todo counts from the table
        val countTodo: Int = dbHandler.countToDo()
        count.setText("You have $countTodo todos")
        add.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    context,
                    AddToDo::class.java
                )
            )
        })
        listView.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            val todo: ToDo = toDos!![position]
            val builder = AlertDialog.Builder(context)
            builder.setTitle(todo.getTitle())
            builder.setMessage(todo.getDescription())
            builder.setPositiveButton(
                "Finished"
            ) { dialog, which ->
                todo.setFinished(System.currentTimeMillis())
                dbHandler.updateSingleToDo(todo)
                startActivity(Intent(context, MainActivity::class.java))
            }
            builder.setNegativeButton(
                "Delete"
            ) { dialog, which ->
                dbHandler.deleteToDo(todo.getId())
                startActivity(Intent(context, MainActivity::class.java))
            }
            builder.setNeutralButton(
                "Update"
            ) { dialog, which ->
                val intent = Intent(
                    context,
                    EditToDo::class.java
                )
                intent.putExtra("id", String.valueOf(todo.getId()))
                startActivity(intent)
            }
            builder.show()
        })
    }
}