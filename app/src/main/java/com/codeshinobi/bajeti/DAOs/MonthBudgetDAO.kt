package com.codeshinobi.bajeti.DAOs

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.codeshinobi.bajeti.Models.MonthBudget

@Dao
interface MonthBudgetDAO {
    @Insert
    fun insertTotalBudget(expense: MonthBudget)
    @Query("SELECT * FROM MonthBudget")
    fun getTotalBudget(): List<MonthBudget>
    @Query("SELECT * FROM MonthBudget WHERE monthNumber = :month")
    fun getMonthBudget(month: Int): LiveData<List<MonthBudget>>
}