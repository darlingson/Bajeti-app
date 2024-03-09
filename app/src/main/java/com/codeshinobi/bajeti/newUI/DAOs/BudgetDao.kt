package com.codeshinobi.bajeti.newUI.DAOs

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.codeshinobi.bajeti.newUI.Entities.Budget

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget)
    @Update
    suspend fun updateBudget(budget: Budget)
    @Delete
    suspend fun deleteBudget(budget: Budget)
    @Query("SELECT * FROM budget WHERE id = :budgetId")
    fun getBudgetById(budgetId: Int): LiveData<Budget>
    @Query("SELECT * FROM budget WHERE month_name = :monthName AND year = :year")
    fun getBudgetsForMonth(monthName: String, year: Int): LiveData<List<Budget>>
    @Query("SELECT * FROM budget")
    fun getAllBudgets(): LiveData<List<Budget>>
}