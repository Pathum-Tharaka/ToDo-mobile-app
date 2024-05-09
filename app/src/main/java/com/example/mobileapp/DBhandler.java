package com.example.mobileapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBhandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "toDo";
    private static final String TABLE_NAME ="toDo";

    //column names
    private static final String ID ="id";
    private static final String TITLE ="title";
    private static final String DESCRIPTION ="description";
    private static final String STARTED ="started";
    private static final String FINISHED ="finished";

    public DBhandler(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String TABLE_CREATE_QUERY = "CREATE TABLE "+TABLE_NAME+" "+
                "("
                +ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +TITLE + "TEXT,"
                +DESCRIPTION + "TEXT,"
                +STARTED + "TEXT,"
                +FINISHED + "TEXT"+
                ");";

        db.execSQL(TABLE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS "+ TABLE_NAME;
        //drop older table if existed
        db.execSQL(DROP_TABLE_QUERY);
        //create tables again
        onCreate(db);
    }
}
