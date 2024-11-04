package com.example.triptuner.model

data class Expense(
    val name: String = "",
    val amount: Double = 0.0,
    val currency: String = "LKR",
    var id: String? = null
)
