package com.example.bdeorga.storage

import android.content.Context
import com.example.bdeorga.model.Card
import org.json.JSONObject
import com.example.bdeorga.storage.utility.file.JSONFileStorage

class CardJSONFileStorage(context: Context) : JSONFileStorage<Card>(context, "card") {
    override fun create(id: Int, obj: Card): Card {
        return Card(id, obj.value, obj.color, obj.description)
    }

    override fun objectToJson(id: Int, obj: Card): JSONObject {
        return JSONObject()
            .put(Card.ID, id)
            .put(Card.VALUE, obj.value)
            .put(Card.COLOR, obj.color)
            .put(Card.DESCRIPTION, obj.description)
    }

    override fun jsonToObject(json: JSONObject): Card {
        return Card(
            json.getInt(Card.ID),
            json.getString(Card.VALUE),
            json.getString(Card.COLOR),
            json.getString(Card.DESCRIPTION),
        )
    }

}