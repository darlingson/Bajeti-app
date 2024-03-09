package com.codeshinobi.bajeti.newUI.DAOs

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.codeshinobi.bajeti.newUI.Entities.Expense

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)
    @Update
    suspend fun updateExpense(expense: Expense)
    @Delete
    suspend fun deleteExpense(expense: Expense)
    @Query("SELECT * FROM expense")
    fun getAllExpenses(): LiveData<List<Expense>>
    @Query("SELECT * FROM expense WHERE id = :expenseId")
    fun getExpenseById(expenseId: Int): LiveData<Expense>
    @Query("SELECT * FROM expense WHERE month = :month AND year = :year")
    fun getExpensesForMonth(month: String, year: Int): LiveData<List<Expense>>
    @Query("SELECT SUM(amount) FROM expense WHERE month = :month AND year = :year")
    fun getTotalExpenseForMonth(month: String, year: Int): LiveData<Double?>
}