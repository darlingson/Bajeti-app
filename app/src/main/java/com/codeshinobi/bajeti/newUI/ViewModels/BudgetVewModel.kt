package com.codeshinobi.bajeti.newUI.ViewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeshinobi.bajeti.newUI.DAOs.BudgetDao
import com.codeshinobi.bajeti.newUI.Entities.Budget
import com.codeshinobi.bajeti.newUI.Expenses_App_DB
import com.codeshinobi.bajeti.newUI.Repositories.BudgetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BudgetViewModel(application:Application) : ViewModel() {
    private val repository: BudgetRepository

    init {
        val db = Expenses_App_DB.getDatabase(application)
        val BudgetDao = db?.budgetDao
        repository = BudgetDao?.let { BudgetRepository(it) }!!
    }

    val allBudgets: LiveData<List<Budget>> = repository.allBudgets
    val curentBudget:LiveData<List<Budget>> = repository.currentBudget
    fun insert(budget: Budget) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(budget)
    }
    fun update(budget: Budget) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(budget)
    }
    fun delete(budget: Budget) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(budget)
    }
}