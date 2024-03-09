package com.codeshinobi.bajeti.newUI.Repositories

import androidx.lifecycle.LiveData
import com.codeshinobi.bajeti.newUI.DAOs.BudgetDao
import com.codeshinobi.bajeti.newUI.Entities.Budget
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BudgetRepository(private val budgetDao: BudgetDao) {
    val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(Calendar.getInstance().time)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val currentBudget: LiveData<List<Budget>> = budgetDao.getBudgetsForMonth(currentMonth, currentYear)
    val allBudgets: LiveData<List<Budget>> = budgetDao.getAllBudgets()
    suspend fun insert(budget: Budget) {
        budgetDao.insertBudget(budget)
    }
    suspend fun update(budget: Budget) {
        budgetDao.updateBudget(budget)
    }
    suspend fun delete(budget: Budget) {
        budgetDao.deleteBudget(budget)
    }
    fun getBudgetById(budgetId: Int): LiveData<Budget> {
        return budgetDao.getBudgetById(budgetId)
    }
    fun getBudgetsForMonth(monthName: String, year: Int): LiveData<List<Budget>> {
        return budgetDao.getBudgetsForMonth(monthName, year)
    }
}