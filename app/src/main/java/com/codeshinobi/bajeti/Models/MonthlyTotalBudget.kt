package com.codeshinobi.bajeti.Models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
*Will contain totals of monthly budgets
*/
@Entity
data class MonthlyTotalBudget(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,
    @ColumnInfo(name ="month_name")
    val monthName:String,
    @ColumnInfo(name ="month_number")
    val monthNumber:Int,
    @ColumnInfo(name ="expense_type")
    val expenseType:String,
    @ColumnInfo(name ="month_budget")
    val monthBudget:Int
)
