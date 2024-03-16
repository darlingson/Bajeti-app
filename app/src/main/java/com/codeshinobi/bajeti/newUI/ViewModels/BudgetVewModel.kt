package com.codeshinobi.bajeti.newUI.ViewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeshinobi.bajeti.newUI.Entities.Budget
import com.codeshinobi.bajeti.newUI.Entities.Expense
import com.codeshinobi.bajeti.newUI.Expenses_App_DB
import com.codeshinobi.bajeti.newUI.Repositories.BudgetRepository
import com.codeshinobi.bajeti.newUI.Repositories.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BudgetViewModel(application: Application) : ViewModel() {
    private val repository: BudgetRepository
    private val expenseRepository: ExpenseRepository

    init {
        val db = Expenses_App_DB.getDatabase(application)
        val BudgetDao = db.budgetDao
        val ExpenseDao = db.expenseDao
        repository = BudgetDao?.let { BudgetRepository(it) }!!
        expenseRepository = ExpenseDao?.let { ExpenseRepository(it) }!!
    }

    val allExpenses: LiveData<List<Expense>> = expenseRepository.allExpenses
    val currentMonthExpenses: LiveData<List<Expense>> = expenseRepository.allCurrentExpenses
    val allBudgets: LiveData<List<Budget>> = repository.allBudgets
    val curentBudget: LiveData<List<Budget>> = repository.currentBudget
    fun insert(budget: Budget) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(budget)
    }

    fun update(budget: Budget) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(budget)
    }

    fun delete(budget: Budget) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(budget)
    }


    fun insert(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        expenseRepository.insert(expense)
    }

    fun update(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        expenseRepository.update(expense)
    }

    fun delete(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        expenseRepository.delete(expense)
    }
}