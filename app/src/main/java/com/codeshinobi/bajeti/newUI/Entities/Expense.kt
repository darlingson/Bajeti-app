package com.codeshinobi.bajeti.newUI.Entities

import java.util.Date

data class Expense(
    val date: Date,
    val name: String,
    val amount: Double,
    val description: String,
    val quantity:Float,
    val category: String,
    val month:String,
    val monthNumber:Int,
    val weekNumber: Int,
    val year:Int
)