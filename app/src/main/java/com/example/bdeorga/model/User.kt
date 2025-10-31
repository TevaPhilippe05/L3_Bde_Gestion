package com.example.bdeorga.model

data class User(
    val id: Int,
    val nom: String,
    val prenom: String,
    val role: String,
    val email: String,
    val telephone: String,
    var profileImageUri: String? = null
)
