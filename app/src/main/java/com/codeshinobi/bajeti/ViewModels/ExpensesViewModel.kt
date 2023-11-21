package com.codeshinobi.bajeti.ViewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeshinobi.bajeti.Models.ExpenseEntity
import com.codeshinobi.bajeti.Models.ExpensesDatabase
import com.codeshinobi.bajeti.Repositories.ExpenseRepository

class ExpensesViewModel(application: Application) : ViewModel() {
    val allExpenses: LiveData<List<ExpenseEntity>>
    private val repository: ExpenseRepository
    val searchResults: MutableLiveData<List<ExpenseEntity>>

    init {
        val expensesDatabase = ExpensesDatabase.getInstance(application)
        val expenseDAO = expensesDatabase?.ExpenseDAO
        repository = expenseDAO?.let { ExpenseRepository(it) }!!

        allExpenses = repository.allExpenses
        searchResults = repository.searchResults
    }
    fun insertProduct(ExpenseEntity: ExpenseEntity) {
        repository.insertExpense(ExpenseEntity)
    }

    fun findProduct(name: String) {
        repository.findExpense(name)
    }

    fun deleteProduct(name: String) {
        repository.deleteExpense(name)
    }
}