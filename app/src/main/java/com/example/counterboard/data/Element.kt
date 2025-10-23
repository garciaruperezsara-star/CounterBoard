package com.example.counterboard.data

data class Element(
    val id: Int,
    var title: String,
    var points: Int,
    val categoryId: Int
) {
    companion object {
        const val TABLE_NAME = "Element"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_POINTS = "points"
        const val COLUMN_BOARD = "boardId"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_TITLE TEXT, " +
                    "$COLUMN_POINTS INTEGER,"+
                    "$COLUMN_BOARD INTEGER,"+
                    "FOREIGN KEY($COLUMN_BOARD) REFERENCES ${Board.TABLE_NAME}(${Board.COLUMN_ID})"+
                    "ON DELETE CASCADE)"

        const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}