package com.example.bdeorga.storage

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.bdeorga.helper.DataBaseHelper
import com.example.bdeorga.model.Card
import com.example.bdeorga.storage.utility.DataBaseStorage

class CardDataBaseStorage(context: Context) : DataBaseStorage<Card>(DataBaseHelper(context), "Card") {

    companion object {
        const val ID = 0
        const val VALUE = 1
        const val COLOR = 2
        const val DESCRIPTION = 3
    }

    override fun objectToValues(obj: Card): ContentValues {
        val values = ContentValues()
        values.put(Card.VALUE, obj.value)
        values.put(Card.COLOR, obj.color)
        values.put(Card.DESCRIPTION, obj.description)
        return values
    }

    override fun cursorToObject(cursor: Cursor): Card {
        return Card(
            id = cursor.getInt(ID),
            value = cursor.getString (VALUE),
            color = cursor.getString (COLOR),
            description = cursor.getString (DESCRIPTION)
        )
    }
}