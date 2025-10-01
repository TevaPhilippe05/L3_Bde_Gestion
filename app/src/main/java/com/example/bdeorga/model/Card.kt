package com.example.bdeorga.model

class Card (var id: Int, val value: String, val color:String, val description:String){
    companion object {
        const val ID = "id"
        const val VALUE = "value"
        const val COLOR = "color"
        const val DESCRIPTION = "description"
    }
}