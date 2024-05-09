package com.example.mobileapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBhandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, VERSION) {

    companion object {
        private const val VERSION = 1
        private const val DB_NAME = "toDo"
        private const val TABLE_NAME = "toDo"

        // Column names
        private const val ID = "id"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val STARTED = "started"
        private const val FINISHED = "finished"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val TABLE_CREATE_QUERY = "CREATE TABLE $TABLE_NAME " +
                "($ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$TITLE TEXT, " +
                "$DESCRIPTION TEXT, " +
                "$STARTED TEXT, " +
                "$FINISHED TEXT" +
                ");"

        db.execSQL(TABLE_CREATE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE_QUERY = "DROP TABLE IF EXISTS $TABLE_NAME"
        // Drop older table if existed
        db.execSQL(DROP_TABLE_QUERY)
        // Create tables again
        onCreate(db)
    }
}