package com.codeshinobi.bajeti.DAOs

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.codeshinobi.bajeti.Models.ExpenseEntity
import com.codeshinobi.bajeti.Models.MonthlyTotalBudget

@Dao
interface MonthlyTotalDAO {
    @Query("SELECT * FROM MonthlyTotalBudget")
    fun getAllTotalBudgets():LiveData<MonthlyTotalBudget>

    @Query("SELECT * FROM MonthlyTotalBudget")
    fun getAllBudgets(): LiveData<List<MonthlyTotalBudget>>

    @Query("SELECT * FROM MonthlyTotalBudget WHERE month_number = :month")
    fun getMonthBudgetTotals(month:Int):LiveData<List<MonthlyTotalBudget>>
    @Query("SELECT SUM(month_budget) FROM MonthlyTotalBudget WHERE month_number = :month")
    fun getTotalMonthTotalBudget(month: Int):LiveData<Int>
    @Insert
    fun insertTotalBudget(expense: MonthlyTotalBudget)
    @Insert
    fun insertAllTotalBudget(vararg expenses:  MonthlyTotalBudget)
}