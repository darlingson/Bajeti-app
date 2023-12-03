package com.codeshinobi.bajeti.ViewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.codeshinobi.bajeti.Models.ExpensesDatabase
import com.codeshinobi.bajeti.Models.MonthBudget
import com.codeshinobi.bajeti.Repositories.MonthlBudgetRepository
class MonthBudgetViewModel(application: Application) : ViewModel() {
    val allBudgets: LiveData<List<MonthBudget>>
    private val repository: MonthlBudgetRepository

    init {
        val expensesDatabase = ExpensesDatabase.getInstance(application)
        val budgetDAO = expensesDatabase?.MonthBudgetDAO
        repository = budgetDAO?.let { MonthlBudgetRepository(it) }!!

        allBudgets = repository.allBudgets
    }

    fun insertMonthBudget(newBudget: MonthBudget) {
        repository.insertBudget(newBudget)
    }
}