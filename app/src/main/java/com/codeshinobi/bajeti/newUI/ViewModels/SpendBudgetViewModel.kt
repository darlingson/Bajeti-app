package com.codeshinobi.bajeti.newUI.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeshinobi.bajeti.newUI.Entities.SpendBudget
import com.codeshinobi.bajeti.newUI.Repositories.SpendBudgetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SpendBudgetViewModel(private val repository: SpendBudgetRepository) : ViewModel() {

    val allSpendBudgets: LiveData<List<SpendBudget>> = repository.allSpendBudgets
    val currentMonthSpendBudgets:LiveData<List<SpendBudget>> = repository.currentSpendBudgets
    fun insert(spendBudget: SpendBudget) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(spendBudget)
    }

    fun update(spendBudget: SpendBudget) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(spendBudget)
    }

    fun delete(spendBudget: SpendBudget) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(spendBudget)
    }
}