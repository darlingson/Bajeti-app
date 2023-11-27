package com.codeshinobi.bajeti.ViewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codeshinobi.bajeti.Models.ExpensesDatabase
import com.codeshinobi.bajeti.Models.OtherExpensesEntity
import com.codeshinobi.bajeti.Repositories.OtherExpensesRepository

class OtherExpensesViewModel(application: Application) {
    val allExpenses: LiveData<List<OtherExpensesEntity>>
    private val repository: OtherExpensesRepository
    val searchResults: MutableLiveData<List<OtherExpensesEntity>>

    init {
        val expensesDatabase = ExpensesDatabase.getInstance(application)
        val otherExpenseDAO = expensesDatabase?.OtherExpensesDAO
        repository = otherExpenseDAO?.let { OtherExpensesRepository(it) }!!

        allExpenses = repository.allOtherExpenses
        searchResults = repository.searchResults
    }
    fun insertOtherExpense(OtherExpensesEntity: OtherExpensesEntity) {
        repository.insertExpense(OtherExpensesEntity)
    }
    fun findOtherExpense(name: String) {
        repository.findExpense(name)
    }
    fun deleteOtherExpense(name: String) {
        repository.deleteExpense(name)
    }
}