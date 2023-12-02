package com.codeshinobi.bajeti.Models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
*The totals of monthly amounts will be added to the total budget of the month
*/
@Entity
data class MonthBudget(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,
    @ColumnInfo(name = "expenseName")val expenseName: String?,
    @ColumnInfo(name = "expenseType")val expenseType: String?,
    @ColumnInfo(name = "monthNumber")val monthNumber: Int?,
    @ColumnInfo(name = "amount")val budgetAmount:Int?
)
