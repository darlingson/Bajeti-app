package com.codeshinobi.bajeti.newUI.Entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Budget(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,
    @ColumnInfo(name = "amount")val amount: Double,
    @ColumnInfo(name = "month_name")val month_name: String,
    @ColumnInfo(name = "month_number")val month_number: Int,
    @ColumnInfo(name = "year")val year: Int
)
