package com.codeshinobi.bajeti.DAOs
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.codeshinobi.bajeti.Models.TransportExpensesEntity

@Dao
interface TransportExpenseDAO {
    @Query("SELECT * FROM TransportExpensesEntity")
    fun getAll(): LiveData<TransportExpensesEntity>

    @Query("SELECT * FROM TransportExpensesEntity")
    fun getAllExpenses(): LiveData<List<TransportExpensesEntity>>

    @Query("SELECT * FROM TransportExpensesEntity WHERE id = :id")
    fun getById(id: Int): TransportExpensesEntity
    @Insert
    fun insert(expense: TransportExpensesEntity)
    @Insert
    fun insertAll(vararg expenses:  TransportExpensesEntity)

    @Query("SELECT * FROM TransportExpensesEntity WHERE location = :location")
    fun findExpense(location: String): List<TransportExpensesEntity>

    @Query("DELETE FROM TransportExpensesEntity WHERE location = :location")
    fun deleteExpense(location: String)

    @Query("SELECT * FROM TransportExpensesEntity")
    fun getAllExpensess(): LiveData<List<TransportExpensesEntity>>
}