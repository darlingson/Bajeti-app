package com.codeshinobi.bajeti.Repositories//package com.codeshinobi.bajeti.Repositories
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codeshinobi.bajeti.DAOs.ExpenseDAO
import com.codeshinobi.bajeti.Models.ExpenseEntity
import kotlinx.coroutines.*

class ExpenseRepository(private val ExpenseDao: ExpenseDAO) {

    val allExpenses: LiveData<List<ExpenseEntity>> = ExpenseDao.getAllExpenses()
    val searchResults = MutableLiveData<List<ExpenseEntity>>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertExpense(newExpense: ExpenseEntity) {
        coroutineScope.launch(Dispatchers.IO) {
            ExpenseDao.insert(newExpense)
        }
    }

    fun deleteExpense(name: String) {
        coroutineScope.launch(Dispatchers.IO) {
            ExpenseDao.deleteExpense(name)
        }
    }


    fun findExpense(name: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(name).await()
        }
    }

    private fun asyncFind(name: String): Deferred<List<ExpenseEntity>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async ExpenseDao.findExpense(name)
        }


}