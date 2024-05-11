package com.example.mobileapp


data class ToDo(
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var started: Long = 0,
    var finished: Long = 0
) {
    constructor() : this(0, "", "", 0, 0)

    constructor(title: String, description: String, started: Long, finished: Long) : this(
        0,
        title,
        description,
        started,
        finished
    )
}