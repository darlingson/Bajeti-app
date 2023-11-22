package com.codeshinobi.bajeti.Models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransportExpensesEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(name = "amount")
    val amount: Int,
    @ColumnInfo(name = "date")
    val date: String
)
