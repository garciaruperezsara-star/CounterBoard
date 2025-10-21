package com.example.counterboard.data

data class Board (
    val id: Int,
    var title: String
){

    companion object {
        const val TABLE_NAME = "Board"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_TITLE TEXT);"
        const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}