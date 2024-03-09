package com.codeshinobi.bajeti.newUI.DAOs

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.codeshinobi.bajeti.newUI.Entities.SpendBudget

@Dao
interface SpendBudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpendBudget(spendBudget: SpendBudget)
    @Update
    suspend fun updateSpendBudget(spendBudget: SpendBudget)
    @Delete
    suspend fun deleteSpendBudget(spendBudget: SpendBudget)
    @Query("SELECT * FROM spendbudget WHERE id = :spendBudgetId")
    fun getSpendBudgetById(spendBudgetId: Int): LiveData<SpendBudget>
    @Query("SELECT * FROM spendbudget WHERE month_name = :monthName AND year = :year")
    fun getSpendBudgetsForMonth(monthName: String, year: Int): LiveData<List<SpendBudget>>
    @Query("SELECT * FROM spendbudget")
    fun getAllSpendBudgets(): LiveData<List<SpendBudget>>
}