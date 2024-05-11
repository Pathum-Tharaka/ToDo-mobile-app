package com.example.mobileapp


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ToDoAdapter(context: Context, private val resource: Int, private val todos: List<ToDo>) :
    ArrayAdapter<ToDo>(context, resource, todos) {

    private val inflater = LayoutInflater.from(context)

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = inflater.inflate(resource, parent, false)

        val title = row.findViewById<TextView>(R.id.title)
        val description = row.findViewById<TextView>(R.id.description)
        val imageView = row.findViewById<ImageView>(R.id.onGoing)

        val toDo = todos[position]
        title.text = toDo.title
        description.text = toDo.description
        imageView.visibility = View.INVISIBLE

        if (toDo.finished > 0) {
            imageView.visibility = View.VISIBLE
        }

        return row
    }
}