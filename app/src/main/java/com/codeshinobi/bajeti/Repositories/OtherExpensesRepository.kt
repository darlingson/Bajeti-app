package com.codeshinobi.bajeti.Repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codeshinobi.bajeti.DAOs.OtherExpensesDAO
import com.codeshinobi.bajeti.Models.OtherExpensesEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class OtherExpensesRepository(private val OtherExpensesDAO: OtherExpensesDAO) {
    val allOtherExpenses: LiveData<List<OtherExpensesEntity>> = OtherExpensesDAO.getAllExpenses()
    val searchResults = MutableLiveData<List<OtherExpensesEntity>>()
    var sumofOtherExpenses: LiveData<Int> = OtherExpensesDAO.getTotalAmount()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertExpense(newExpense: OtherExpensesEntity) {
        coroutineScope.launch(Dispatchers.IO) {
            OtherExpensesDAO.insert(newExpense)
        }
    }

    fun deleteExpense(name: String) {
        coroutineScope.launch(Dispatchers.IO) {
            OtherExpensesDAO.deleteOtherExpense(name)
        }
    }

    fun findExpense(name: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(name).await()
        }
    }

    private fun asyncFind(name: String): Deferred<List<OtherExpensesEntity>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async OtherExpensesDAO.findOtherExpense(name)
        }
}