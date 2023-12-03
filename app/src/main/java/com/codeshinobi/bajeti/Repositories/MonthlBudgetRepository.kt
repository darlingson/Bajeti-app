package com.codeshinobi.bajeti.Repositories

import androidx.lifecycle.LiveData
import com.codeshinobi.bajeti.DAOs.MonthBudgetDAO
import com.codeshinobi.bajeti.Models.MonthBudget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class MonthlBudgetRepository(private val monthBudgetDao: MonthBudgetDAO) {
    val cal:Calendar = Calendar.getInstance()
    val year:Int = cal.get(Calendar.YEAR)
    val month:Int = cal.get(Calendar.MONTH)
    val day:Int = cal.get(Calendar.DATE)
    val currentIndex = (year - 1970) * 12 + month

    val allBudgets: LiveData<List<MonthBudget>> = monthBudgetDao.getMonthBudget(currentIndex)
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertBudget(monthBudget: MonthBudget){
        coroutineScope.launch(Dispatchers.IO) {
            monthBudgetDao.insertTotalBudget(monthBudget)
        }
    }
}