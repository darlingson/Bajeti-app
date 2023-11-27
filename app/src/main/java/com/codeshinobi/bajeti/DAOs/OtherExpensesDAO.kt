package com.codeshinobi.bajeti.DAOs

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.codeshinobi.bajeti.Models.OtherExpensesEntity
@Dao
interface OtherExpensesDAO {
    @Query("SELECT * FROM OtherExpensesEntity")
    fun getAll(): LiveData<OtherExpensesEntity>

    @Query("SELECT * FROM OtherExpensesEntity")
    fun getAllExpenses(): LiveData<List<OtherExpensesEntity>>

    @Query("SELECT * FROM OtherExpensesEntity WHERE id = :id")
    fun getById(id: Int): OtherExpensesEntity
    @Insert
    fun insert(expense: OtherExpensesEntity)
    @Insert
    fun insertAll(vararg expenses:  OtherExpensesEntity)

    @Query("SELECT * FROM OtherExpensesEntity WHERE name = :name")
    fun findOtherExpense(name: String): List<OtherExpensesEntity>

    @Query("DELETE FROM OtherExpensesEntity WHERE name = :name")
    fun deleteOtherExpense(name: String)

    @Query("SELECT * FROM OtherExpensesEntity")
    fun getAllOtherExpensess(): LiveData<List<OtherExpensesEntity>>

    @Query("SELECT SUM(amount) FROM OtherExpensesEntity")
    fun getTotalAmount(): LiveData<Int>
}