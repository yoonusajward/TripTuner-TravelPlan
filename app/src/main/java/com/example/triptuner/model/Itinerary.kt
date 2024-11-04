package com.example.triptuner.model

import com.google.firebase.firestore.DocumentId

data class Itinerary(
    @DocumentId
    val id: String = "", // Firestorm document ID
    val destination: String = "",
    val budget: String = "",
    val companions: String = "",
    val specialNotes: String = "",
    val generatedInformation: String = "",
    val edited: Boolean = false
)
