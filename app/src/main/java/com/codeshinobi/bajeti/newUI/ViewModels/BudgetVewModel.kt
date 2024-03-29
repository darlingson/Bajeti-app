package com.codeshinobi.bajeti.newUI.ViewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeshinobi.bajeti.newUI.Entities.Budget
import com.codeshinobi.bajeti.newUI.Entities.Expense
import com.codeshinobi.bajeti.newUI.Entities.SpendBudget
import com.codeshinobi.bajeti.newUI.Expenses_App_DB
import com.codeshinobi.bajeti.newUI.Repositories.BudgetRepository
import com.codeshinobi.bajeti.newUI.Repositories.ExpenseRepository
import com.codeshinobi.bajeti.newUI.Repositories.SpendBudgetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BudgetViewModel(application: Application) : ViewModel() {
    private val repository: BudgetRepository
    private val expenseRepository: ExpenseRepository
    private val spendRepository: SpendBudgetRepository

    init {
        val db = Expenses_App_DB.getDatabase(application)
        val BudgetDao = db.budgetDao
        val ExpenseDao = db.expenseDao
        val SpendBudgetDao = db.spendBudgetDao
        repository = BudgetDao?.let { BudgetRepository(it) }!!
        expenseRepository = ExpenseDao?.let { ExpenseRepository(it) }!!
        spendRepository = SpendBudgetDao?.let { SpendBudgetRepository(it) }!!
    }

    val allExpenses: LiveData<List<Expense>> = expenseRepository.allExpenses
    val currentMonthExpenses: LiveData<List<Expense>> = expenseRepository.allCurrentExpenses
    val allBudgets: LiveData<List<Budget>> = repository.allBudgets
    val curentBudget: LiveData<List<Budget>> = repository.currentBudget
    val allSpendBudgets: LiveData<List<SpendBudget>> = spendRepository.allSpendBudgets
    val currentMonthSpendBudgets:LiveData<List<SpendBudget>> = spendRepository.currentSpendBudgets
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

    fun insert(spendBudget: SpendBudget) = viewModelScope.launch(Dispatchers.IO) {
        spendRepository.insert(spendBudget)
    }

    fun update(spendBudget: SpendBudget) = viewModelScope.launch(Dispatchers.IO) {
        spendRepository.update(spendBudget)
    }

    fun delete(spendBudget: SpendBudget) = viewModelScope.launch(Dispatchers.IO) {
        spendRepository.delete(spendBudget)
    }
}