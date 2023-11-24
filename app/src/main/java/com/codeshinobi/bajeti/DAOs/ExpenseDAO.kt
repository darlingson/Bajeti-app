package com.codeshinobi.bajeti.DAOs
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.codeshinobi.bajeti.Models.ExpenseEntity

@Dao
interface ExpenseDAO {
    @Query("SELECT * FROM ExpenseEntity")
    fun getAll(): LiveData<ExpenseEntity>

    @Query("SELECT * FROM ExpenseEntity")
    fun getAllExpenses(): LiveData<List<ExpenseEntity>>

    @Query("SELECT * FROM ExpenseEntity WHERE id = :id")
    fun getById(id: Int): ExpenseEntity
    @Insert
    fun insert(expense: ExpenseEntity)
    @Insert
    fun insertAll(vararg expenses:  ExpenseEntity)

    @Query("SELECT * FROM ExpenseEntity WHERE name = :name")
    fun findExpense(name: String): List<ExpenseEntity>

    @Query("DELETE FROM ExpenseEntity WHERE name = :name")
    fun deleteExpense(name: String)

    @Query("SELECT * FROM ExpenseEntity")
    fun getAllExpensess(): LiveData<List<ExpenseEntity>>

    @Query("SELECT SUM(amount) FROM ExpenseEntity")
    fun getTotalAmount(): LiveData<Int>
}