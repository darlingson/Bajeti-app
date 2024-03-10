package com.codeshinobi.bajeti.newUI.Entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
@Entity
data class Expense(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,
    @ColumnInfo(name = "date")val date: String,
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "amount")val amount: Double,
    @ColumnInfo(name = "description")val description: String,
    @ColumnInfo(name = "quantity")val quantity:Float,
    @ColumnInfo(name = "category")val category: String,
    @ColumnInfo(name = "month")val month:String,
    @ColumnInfo(name ="month_number")val monthNumber:Int,
    @ColumnInfo(name = "week_number")val weekNumber: Int,
    @ColumnInfo(name = "year")val year:Int
)