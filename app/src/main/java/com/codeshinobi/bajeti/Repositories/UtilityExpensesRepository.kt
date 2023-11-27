package com.codeshinobi.bajeti.Repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codeshinobi.bajeti.DAOs.TransportExpenseDAO
import com.codeshinobi.bajeti.DAOs.UtilitiesDAO
import com.codeshinobi.bajeti.Models.TransportExpensesEntity
import com.codeshinobi.bajeti.Models.UtilitiesEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class UtilityExpensesRepository (private val UtilitiesDAO: UtilitiesDAO){
    val allTransportExpenses: LiveData<List<UtilitiesEntity>> = UtilitiesDAO.getAllUtilitiesExpenses()
    val searchResults = MutableLiveData<List<UtilitiesEntity>>()
    var sumofTransportExpenses: LiveData<Int> = UtilitiesDAO.getTotalUtilitiesAmount()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
}