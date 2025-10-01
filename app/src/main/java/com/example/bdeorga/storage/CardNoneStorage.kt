package com.example.bdeorga.storage

import com.example.bdeorga.model.Card
import com.example.bdeorga.storage.utility.Storage

class CardNoneStorage : Storage<Card> {
    override fun insert(obj: Card): Int = 0

    override fun size(): Int = 0

    override fun find(id: Int): Card? = null

    override fun findAll(): List<Card> = emptyList()

    override fun delete(id: Int) = Unit

    override fun update(id: Int, obj: Card) = Unit
}