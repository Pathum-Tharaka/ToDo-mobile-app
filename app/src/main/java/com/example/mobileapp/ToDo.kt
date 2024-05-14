package com.example.mobileapp

// Data class representing a ToDo item
data class ToDo(
    var id: Int = 0, // Unique identifier for the ToDo item
    var title: String = "", // Title of the ToDo item
    var description: String = "", // Description of the ToDo item
    var started: Long = 0, // Timestamp indicating when the ToDo item was started
    var finished: Long = 0 // Timestamp indicating when the ToDo item was finished
) {
    // Secondary constructors for convenience
    constructor() : this(0, "", "", 0, 0)

    constructor(title: String, description: String, started: Long, finished: Long) : this(
        0,
        title,
        description,
        started,
        finished
    )
}
