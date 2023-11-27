package com.codeshinobi.bajeti.DAOs

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.codeshinobi.bajeti.Models.UtilitiesEntity
@Dao
interface UtilitiesDAO {
    @Query("SELECT * FROM UtilitiesEntity")
    fun getAllUtilities(): LiveData<UtilitiesEntity>

    @Query("SELECT * FROM UtilitiesEntity")
    fun getAllUtilitiesExpenses(): LiveData<List<UtilitiesEntity>>

    @Query("SELECT * FROM UtilitiesEntity WHERE id = :id")
    fun getUtilitiesById(id: Int): UtilitiesEntity
    @Insert
    fun insertUtilities(expense: UtilitiesEntity)
    @Insert
    fun insertAllUtilities(vararg expenses: UtilitiesEntity)

    @Query("SELECT * FROM UtilitiesEntity WHERE name = :name")
    fun findUtilitiesExpense(name: String): List<UtilitiesEntity>

    @Query("DELETE FROM UtilitiesEntity WHERE name = :name")
    fun deleteUtilitiesExpense(name: String)

    @Query("SELECT * FROM UtilitiesEntity")
    fun getAllUtilitiesExpensess(): LiveData<List<UtilitiesEntity>>

    @Query("SELECT SUM(amount) FROM UtilitiesEntity")
    fun getTotalUtilitiesAmount(): LiveData<Int>
}