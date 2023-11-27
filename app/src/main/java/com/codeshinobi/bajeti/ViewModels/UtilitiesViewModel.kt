package com.codeshinobi.bajeti.ViewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeshinobi.bajeti.Models.ExpensesDatabase
import com.codeshinobi.bajeti.Models.UtilitiesEntity
import com.codeshinobi.bajeti.Repositories.UtilityExpensesRepository

class UtilitiesViewModel(application: Application) : ViewModel() {
    val allExpenses: LiveData<List<UtilitiesEntity>>
    private val repository: UtilityExpensesRepository
    val searchResults: MutableLiveData<List<UtilitiesEntity>>

    init {
        val expensesDatabase = ExpensesDatabase.getInstance(application)
        val utilitiesDAO = expensesDatabase?.UtilitiesDAO
        repository = utilitiesDAO?.let { UtilityExpensesRepository(it) }!!

        allExpenses = repository.allUtilityExpenses
        searchResults = repository.searchResults
    }

    fun insertUtilitiesExpense(newExpense: UtilitiesEntity) {
        repository.insertUtilitiesExpense(newExpense)
    }

    fun deleteUtilitiesExpense(name: String) {
        repository.deleteUtilitiesExpense(name)
    }

    fun findUtilitiesExpense(name: String) {
        repository.findUtilitiesExpense(name)
    }
}