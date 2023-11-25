package com.codeshinobi.bajeti.Repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codeshinobi.bajeti.DAOs.TransportExpenseDAO
import com.codeshinobi.bajeti.Models.TransportExpensesEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TransportExpensesRepository(private val transportExpenseDAO: TransportExpenseDAO) {

    val allTransportExpenses: LiveData<List<TransportExpensesEntity>> = transportExpenseDAO.getAllExpenses()
    val searchResults = MutableLiveData<List<TransportExpensesEntity>>()
    var sumofTransportExpenses:Int = 0
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertExpense(newExpense: TransportExpensesEntity) {
        coroutineScope.launch(Dispatchers.IO) {
            transportExpenseDAO.insert(newExpense)
        }
    }

    fun deleteExpense(name: String) {
        coroutineScope.launch(Dispatchers.IO) {
            transportExpenseDAO.deleteTransportExpense(name)
        }
    }

    fun findExpense(name: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(name).await()
        }
    }

    fun getSumofExpenses():Int {
        coroutineScope.launch {
            sumofTransportExpenses = transportExpenseDAO.getTotalAmount().value!!
        }
        return sumofTransportExpenses
    }

    private fun asyncFind(name: String): Deferred<List<TransportExpensesEntity>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async transportExpenseDAO.findTransportExpense(name)
        }

}