package com.example.counterboard.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.counterboard.utils.DataBaseManager

class BoardDAO(val context: Context) {

    private lateinit var db: SQLiteDatabase

    private fun open() {
        db = DataBaseManager(context).writableDatabase
    }
    private fun close() {
        db.close()
    }

    fun insert(board : Board) {
        val values = ContentValues()
        values.put(Board.COLUMN_TITLE, board.title)

        try {
            open()
            val newRowId = db.insert(Board.TABLE_NAME, null, values)
            Log.i("DATABASE", "New row inserted in table ${Board.TABLE_NAME} with id: $newRowId")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun update(board:Board) {
        val values = ContentValues()
        values.put(Board.COLUMN_TITLE, board.title)

        try {
            open()
            val updatedRows = db.update(
                Board.COLUMN_TITLE,
                values,
                "${Board.COLUMN_ID} = ${board.id}",
                null
            )
            Log.i("DATABASE", "$updatedRows rows updated in table ${Board.TABLE_NAME}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun delete(id: Int) {
        try {
            open()

            // Insert the new row, returning the primary key value of the new row
            val deletedRows = db.delete(Board.COLUMN_TITLE, "${Board.COLUMN_ID} = $id", null)
            Log.i("DATABASE", "$deletedRows rows deleted in table ${Board.TABLE_NAME}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun findAll(): List<Board> {
        return findBy(null)
    }

    fun findById(id: Int?): Board? {
        var board: Board? = null
        val projection = null
        val selection = "${Board.COLUMN_ID} = $id"
        val selectionArgs = null
        val sortOrder = null
        try {
            open()
            val cursor = db.query(
                Board.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
            if (cursor.moveToNext()) {
                board = readFromCursor(cursor)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
        return board
    }


    fun findBy(where: String?): List<Board> {
        val items: MutableList<Board> = mutableListOf()
        val projection = null
        val selectionArgs = null
        val sortOrder = null

        try {
            open()
            val cursor = db.query(
                Board.TABLE_NAME,
                projection,
                where,
                selectionArgs,
                null,
                null,
                sortOrder
            )

            // Read the cursor data
            while (cursor.moveToNext()) {
                val board = readFromCursor(cursor)
                items.add(board)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
        return items
    }

    private fun readFromCursor(cursor: Cursor): Board {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(Board.COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(Board.COLUMN_TITLE))
        return Board(id, title)
    }


}
