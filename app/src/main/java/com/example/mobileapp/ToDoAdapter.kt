package com.example.mobileapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView

class ToDoAdapter(
    private val context: Context,
    private val resource: Int,
    private var allTodos: MutableList<ToDo>
) : ArrayAdapter<ToDo>(context, resource, allTodos) {

    private val inflater = LayoutInflater.from(context)
    private var filteredTodos: MutableList<ToDo> = allTodos

    override fun getCount(): Int {
        return filteredTodos.size
    }

    override fun getItem(position: Int): ToDo? {
        return filteredTodos[position]
    }

    override fun getItemId(position: Int): Long {
        return filteredTodos[position].id.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = inflater.inflate(resource, parent, false)

        val title = row.findViewById<TextView>(R.id.title)
        val description = row.findViewById<TextView>(R.id.description)
        val imageView = row.findViewById<ImageView>(R.id.onGoing)

        val toDo = filteredTodos[position]
        title.text = toDo.title
        description.text = toDo.description
        imageView.visibility = if (toDo.finished > 0) View.VISIBLE else View.INVISIBLE

        return row
    }

    override fun getFilter(): Filter {
        return myFilter
    }

    private val myFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            if (constraint == null || constraint.isEmpty()) {
                filterResults.values = allTodos
                filterResults.count = allTodos.size
            } else {
                val filteredList = mutableListOf<ToDo>()
                for (todo in allTodos) {
                    if (todo.title.contains(constraint, true) || todo.description.contains(constraint, true)) {
                        filteredList.add(todo)
                    }
                }
                filterResults.values = filteredList
                filterResults.count = filteredList.size
            }
            return filterResults
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredTodos = results?.values as MutableList<ToDo>
            notifyDataSetChanged()
        }
    }
}
