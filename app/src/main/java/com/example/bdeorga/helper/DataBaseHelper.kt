package com.example.bdeorga.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.bdeorga.model.Card

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, "myppoker.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE Card (" +
                    "${BaseColumns._ID} INTEGER," +
                    "${Card.VALUE} TEXT," +
                    "${Card.COLOR} TEXT," +
                    "${Card.DESCRIPTION} TEXT," +
                    "PRIMARY KEY(${BaseColumns._ID})" +
                    ")"
        )
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}