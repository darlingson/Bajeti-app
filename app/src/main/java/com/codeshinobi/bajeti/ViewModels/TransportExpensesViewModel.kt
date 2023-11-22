package com.codeshinobi.bajeti.ViewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeshinobi.bajeti.Models.TransportExpensesEntity
import com.codeshinobi.bajeti.Models.ExpensesDatabase
import com.codeshinobi.bajeti.Repositories.TransportExpensesRepository

class TransportExpensesViewModel(application: Application) : ViewModel() {
    val allExpenses: LiveData<List<TransportExpensesEntity>>
    private val repository: TransportExpensesRepository
    val searchResults: MutableLiveData<List<TransportExpensesEntity>>

    init {
        val expensesDatabase = ExpensesDatabase.getInstance(application)
        val transportExpenseDAO = expensesDatabase?.TransportExpenseDAO
        repository = transportExpenseDAO?.let { TransportExpensesRepository(it) }!!

        allExpenses = repository.allTransportExpenses
        searchResults = repository.searchResults
    }
    fun insertTransportExpense(TransportExpensesEntity: TransportExpensesEntity) {
        repository.insertExpense(TransportExpensesEntity)
    }

    fun findTransportExpense(name: String) {
        repository.findExpense(name)
    }

    fun deleteTransportExpense(name: String) {
        repository.deleteExpense(name)
    }
}