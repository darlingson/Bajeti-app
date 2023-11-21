package com.codeshinobi.bajeti.DAOs
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.codeshinobi.bajeti.Models.ExpenseEntity

@Dao
interface ExpenseDAO {
    @Query("SELECT * FROM ExpenseEntity")
    fun getAll(): List<ExpenseEntity>
    @Query("SELECT * FROM ExpenseEntity WHERE id = :id")
    fun getById(id: Int): ExpenseEntity
    @Insert
    fun insert(expense: ExpenseEntity)
    @Insert
    fun insertAll(vararg expenses:  ExpenseEntity)
}