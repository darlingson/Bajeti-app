package com.codeshinobi.bajeti.DAOs

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import com.codeshinobi.bajeti.Models.UtilitiesEntity

interface UtilitiesDAO {
    @Query("SELECT * FROM UtilitiesEntity")
    fun getAll(): LiveData<UtilitiesEntity>

    @Query("SELECT * FROM UtilitiesEntity")
    fun getAllExpenses(): LiveData<List<UtilitiesEntity>>

    @Query("SELECT * FROM UtilitiesEntity WHERE id = :id")
    fun getById(id: Int): UtilitiesEntity
    @Insert
    fun insert(expense: UtilitiesEntity)
    @Insert
    fun insertAll(vararg expenses: UtilitiesEntity)

    @Query("SELECT * FROM UtilitiesEntity WHERE name = :name")
    fun findTransportExpense(name: String): List<UtilitiesEntity>

    @Query("DELETE FROM UtilitiesEntity WHERE name = :name")
    fun deleteTransportExpense(name: String)

    @Query("SELECT * FROM UtilitiesEntity")
    fun getAllTransportExpensess(): LiveData<List<UtilitiesEntity>>

    @Query("SELECT SUM(amount) FROM UtilitiesEntity")
    fun getTotalAmount(): LiveData<Int>
}