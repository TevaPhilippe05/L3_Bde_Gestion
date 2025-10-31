package com.example.bdeorga.model

data class Evenement(
    val id: Int,
    val titre: String,
    val description: String,
    val date: String,
    val heure: String,
    val lieu: String,
    val statut: String,
    val membresAttribues: List<Int>,
    val budget: Double,
    val nombreParticipantsPrevus: Int,
    val categorie: String
)
