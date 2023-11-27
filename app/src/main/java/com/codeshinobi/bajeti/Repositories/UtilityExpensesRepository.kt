package com.codeshinobi.bajeti.Repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codeshinobi.bajeti.DAOs.UtilitiesDAO
import com.codeshinobi.bajeti.Models.UtilitiesEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UtilityExpensesRepository (private val UtilitiesDAO: UtilitiesDAO){
    val allUtilityExpenses: LiveData<List<UtilitiesEntity>> = UtilitiesDAO.getAllUtilitiesExpenses()
    val searchResults = MutableLiveData<List<UtilitiesEntity>>()
    var sumofUtilityExpenses: LiveData<Int> = UtilitiesDAO.getTotalUtilitiesAmount()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertUtilitiesExpense(newExpense: UtilitiesEntity) {
        coroutineScope.launch(Dispatchers.IO) {
            UtilitiesDAO.insertUtilities(newExpense)
        }
    }

    fun deleteUtilitiesExpense(name: String) {
        coroutineScope.launch(Dispatchers.IO) {
            UtilitiesDAO.deleteUtilitiesExpense(name)
        }
    }

    fun findUtilitiesExpense(name: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(name).await()
        }
    }
    private fun asyncFind(name: String): Deferred<List<UtilitiesEntity>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async UtilitiesDAO.findUtilitiesExpense(name)
        }
}