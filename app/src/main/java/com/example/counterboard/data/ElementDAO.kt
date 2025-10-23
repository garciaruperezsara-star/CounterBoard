package com.example.counterboard.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.counterboard.utils.DataBaseManager

class ElementDAO(val context: Context) {
    private lateinit var db: SQLiteDatabase

    private fun open() {
        db = DataBaseManager(context).writableDatabase
    }
    private fun close() {
        db.close()
    }

    fun insert(title: String, boardId: Int?) {
        val values = ContentValues()
        values.put(Element.COLUMN_TITLE,title)
        values.put(Element.COLUMN_POINTS, "00")
        values.put(Element.COLUMN_BOARD, boardId)
        try {
            open()
            val newRowId = db.insert(Element.TABLE_NAME, null, values)
            Log.i("DATABASE", "New row inserted in table ${Element.TABLE_NAME} with id: $newRowId")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }
//TODO Optimeze for only commit the points by ++
    fun update(element: Element, points: Int, board_id: Int?) {
        val values = ContentValues()
        values.put(Element.COLUMN_ID, element.id)
        values.put(Element.COLUMN_TITLE, element.title)
        values.put(Element.COLUMN_POINTS, points)
        values.put(Element.COLUMN_BOARD, board_id)

        try {
            open()
            val updatedRows =
                db.update(Element.TABLE_NAME, values, "${Element.COLUMN_ID} = ${element.id}", null)
            Log.i("DATABASE", "$updatedRows rows updated in table ${Element.TABLE_NAME}")
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
            val deletedRows = db.delete(Element.TABLE_NAME, "${Element.COLUMN_ID} = $id", null)
            Log.i("DATABASE", "$deletedRows rows deleted in table ${Element.TABLE_NAME}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun findAll(): List<Element> {
        return findBy(null)
    }

    fun findById(id: Int): Element? {
        var element: Element? = null
        val projection = null
        val selection = "${Element.COLUMN_ID} = $id"
        val selectionArgs = null
        val sortOrder = null
        try {
            open()

            val cursor = db.query(
                Element.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )

            // Read the cursor data
            if (cursor.moveToNext()) {
                element = readFromCursor(cursor)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }

        return element
    }

    fun findAllByBoard(board: Board): List<Element> {
        return findBy("${Element.COLUMN_BOARD} = ${board.id}")
    }

    fun findBy(where: String?): List<Element> {
        val items: MutableList<Element> = mutableListOf()
        val projection = null
        val selectionArgs = null
        val sortOrder = null

        try {
            open()
            val cursor = db.query(
                Element.TABLE_NAME,
                projection,
                where,
                selectionArgs,
                null,
                null,
                sortOrder
            )

            // Read the cursor data
            while (cursor.moveToNext()) {
                val element = readFromCursor(cursor)
                items.add(element)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
        return items
    }

    private fun readFromCursor(cursor: Cursor): Element {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(Element.COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(Element.COLUMN_TITLE))
        val points = cursor.getInt(cursor.getColumnIndexOrThrow(Element.COLUMN_POINTS))
        val boardId = cursor.getInt(cursor.getColumnIndexOrThrow(Element.COLUMN_BOARD))
        return Element(id, title, points, boardId)
    }

}
