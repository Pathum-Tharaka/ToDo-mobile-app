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

// ToDoAdapter class for custom ArrayAdapter to display ToDo items in a ListView
class ToDoAdapter(
    private val context: Context,
    private val resource: Int,
    private var allTodos: MutableList<ToDo>
) : ArrayAdapter<ToDo>(context, resource, allTodos) {

    // LayoutInflater for inflating layout XML
    private val inflater = LayoutInflater.from(context)
    private var filteredTodos: MutableList<ToDo> = allTodos

    // Get the count of filtered todos
    override fun getCount(): Int {
        return filteredTodos.size
    }

    // Get a ToDo item at a given position
    override fun getItem(position: Int): ToDo? {
        return filteredTodos[position]
    }

    // Get the ID of a ToDo item at a given position
    override fun getItemId(position: Int): Long {
        return filteredTodos[position].id.toLong()
    }

    // Create a view for a ToDo item at a given position
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = inflater.inflate(resource, parent, false)

        // Initialize views from layout XML
        val title = row.findViewById<TextView>(R.id.title)
        val description = row.findViewById<TextView>(R.id.description)
        val imageView = row.findViewById<ImageView>(R.id.onGoing)

        // Get the ToDo item at the current position
        val toDo = filteredTodos[position]

        // Set title and description text
        title.text = toDo.title
        description.text = toDo.description

        // Set visibility of 'onGoing' indicator based on the finished status of the ToDo item
        imageView.visibility = if (toDo.finished > 0) View.VISIBLE else View.INVISIBLE

        return row
    }

    // Get filter for filtering ToDo items
    override fun getFilter(): Filter {
        return myFilter
    }

    // Custom Filter implementation
    private val myFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            if (constraint == null || constraint.isEmpty()) {
                // If the constraint is empty, return all Todos
                filterResults.values = allTodos
                filterResults.count = allTodos.size
            } else {
                // Filter Todos based on title or description containing the constraint
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

        // Publish the filtered results
        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredTodos = results?.values as MutableList<ToDo>
            notifyDataSetChanged()
        }
    }
}
