package com.example.counterboard.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.counterboard.data.Board
import com.example.counterboard.data.Element

class DataBaseManager(context: Context): SQLiteOpenHelper(context, "user_boards.db", null, 1){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("PRAGMA foreign_keys= ON")
        db.execSQL(Board.SQL_CREATE_TABLE)
        db.execSQL(Element.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onDestroy(db)
        onCreate(db)
    }
    fun onDestroy(db: SQLiteDatabase) {
        db.execSQL(Element.SQL_DROP_TABLE)
        db.execSQL(Board.SQL_DROP_TABLE)
    }

}