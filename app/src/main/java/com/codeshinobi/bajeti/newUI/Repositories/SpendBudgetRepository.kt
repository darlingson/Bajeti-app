package com.codeshinobi.bajeti.newUI.Repositories

import androidx.lifecycle.LiveData
import com.codeshinobi.bajeti.newUI.DAOs.SpendBudgetDao
import com.codeshinobi.bajeti.newUI.Entities.SpendBudget
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SpendBudgetRepository(private val spendBudgetDao: SpendBudgetDao) {
    val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(Calendar.getInstance().time)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val currentSpendBudgets: LiveData<List<SpendBudget>> = spendBudgetDao.getSpendBudgetsForMonth(currentMonth, currentYear)
    val allSpendBudgets: LiveData<List<SpendBudget>> = spendBudgetDao.getAllSpendBudgets()
    suspend fun insert(spendBudget: SpendBudget) {
        spendBudgetDao.insertSpendBudget(spendBudget)
    }
    suspend fun update(spendBudget: SpendBudget) {
        spendBudgetDao.updateSpendBudget(spendBudget)
    }
    suspend fun delete(spendBudget: SpendBudget) {
        spendBudgetDao.deleteSpendBudget(spendBudget)
    }
    fun getSpendBudgetById(spendBudgetId: Int): LiveData<SpendBudget> {
        return spendBudgetDao.getSpendBudgetById(spendBudgetId)
    }
    fun getSpendBudgetsForMonth(monthName: String, year: Int): LiveData<List<SpendBudget>> {
        return spendBudgetDao.getSpendBudgetsForMonth(monthName, year)
    }
}