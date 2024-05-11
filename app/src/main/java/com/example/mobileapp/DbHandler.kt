package com.example.mobileapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DbHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, VERSION) {

    companion object {
        private const val VERSION = 1
        private const val DB_NAME = "todo"
        private const val TABLE_NAME = "todo"

        // Column names
        private const val ID = "id"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val STARTED = "started"
        private const val FINISHED = "finished"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val TABLE_CREATE_QUERY = "CREATE TABLE $TABLE_NAME (" +
                "$ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$TITLE TEXT, " +
                "$DESCRIPTION TEXT, " +
                "$STARTED TEXT, " +
                "$FINISHED TEXT" +
                ");"

        db.execSQL(TABLE_CREATE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE_QUERY = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(DROP_TABLE_QUERY)
        onCreate(db)
    }

    fun addToDo(toDo: ToDo) {
        val sqLiteDatabase = writableDatabase

        val contentValues = ContentValues().apply {
            put(TITLE, toDo.title)
            put(DESCRIPTION, toDo.description)
            put(STARTED, toDo.started)
            put(FINISHED, toDo.finished)
        }

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues)
        sqLiteDatabase.close()
    }

    fun countToDo(): Int {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        return cursor.count
    }

    fun getAllToDos(): List<ToDo> {
        val toDos = mutableListOf<ToDo>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val toDo = ToDo(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(3),
                    cursor.getLong(4)
                )
                toDos.add(toDo)
            } while (cursor.moveToNext())
        }

        return toDos
    }

    fun deleteToDo(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "id =?", arrayOf(id.toString()))
        db.close()
    }

    fun getSingleToDo(id: Int): ToDo? {
        val db = writableDatabase

        val cursor = db.query(
            TABLE_NAME, arrayOf(ID, TITLE, DESCRIPTION, STARTED, FINISHED),
            "$ID = ?", arrayOf(id.toString()), null, null, null
        )

        return if (cursor != null) {
            cursor.moveToFirst()
            ToDo(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getLong(3),
                cursor.getLong(4)
            )
        } else {
            null
        }
    }

    fun updateSingleToDo(toDo: ToDo): Int {
        val db = writableDatabase

        val contentValues = ContentValues().apply {
            put(TITLE, toDo.title)
            put(DESCRIPTION, toDo.description)
            put(STARTED, toDo.started)
            put(FINISHED, toDo.finished)
        }

        val status = db.update(TABLE_NAME, contentValues, "$ID =?", arrayOf(toDo.id.toString()))
        db.close()
        return status
    }
}