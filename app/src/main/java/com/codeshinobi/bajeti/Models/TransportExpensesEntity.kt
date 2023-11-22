package com.codeshinobi.bajeti.Models

import androidx.room.Entity

@Entity
data class TransportExpensesEntity(
    val id: Int,
    val location: String,
    val amount: Int,
    val date: String
)
