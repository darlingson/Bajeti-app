package com.codeshinobi.bajeti.newUI.Entities

data class SpendBudget(
    val amount: Double,
    val spendCategory: String,
    val monthName: String,
    val monthNumber: Int,
    val year: Int
)
