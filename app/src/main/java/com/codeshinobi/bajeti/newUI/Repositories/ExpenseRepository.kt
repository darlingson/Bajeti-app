package com.codeshinobi.bajeti.newUI.Repositories

import androidx.lifecycle.LiveData
import com.codeshinobi.bajeti.newUI.DAOs.ExpenseDao
import com.codeshinobi.bajeti.newUI.Entities.Expense
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(Calendar.getInstance().time)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()
    val allCurrentExpenses: LiveData<List<Expense>> = expenseDao.getExpensesForMonth(currentMonth, currentYear)
    suspend fun insert(expense: Expense) {
        expenseDao.insertExpense(expense)
    }
    suspend fun update(expense: Expense) {
        expenseDao.updateExpense(expense)
    }
    suspend fun delete(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }
    fun getExpenseById(expenseId: Int): LiveData<Expense> {
        return expenseDao.getExpenseById(expenseId)
    }
    fun getExpensesForMonth(month: String, year: Int): LiveData<List<Expense>> {
        return expenseDao.getExpensesForMonth(month, year)
    }
    fun getTotalExpenseForMonth(month: String, year: Int): LiveData<Double?> {
        return expenseDao.getTotalExpenseForMonth(month, year)
    }
}