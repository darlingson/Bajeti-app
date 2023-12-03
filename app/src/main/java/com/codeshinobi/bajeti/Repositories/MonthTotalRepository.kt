package com.codeshinobi.bajeti.Repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codeshinobi.bajeti.DAOs.MonthlyTotalDAO
import com.codeshinobi.bajeti.Models.MonthlyTotalBudget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MonthTotalRepository(private val totalDAO : MonthlyTotalDAO) {

//    val monthTotalBudget :LiveData<Int> = totalDAO.getTotalMonthTotalBudget()
    val monthTotalBudget = MutableLiveData<LiveData<Int>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun gettotalBudget(month:Int):LiveData<Int>? {
        coroutineScope.launch(Dispatchers.Main) {
            monthTotalBudget.value = asyncFindTotalBudget(month).await()
        }
        return monthTotalBudget.value
    }
    private fun asyncFindTotalBudget(month: Int): Deferred<LiveData<Int>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async totalDAO.getTotalMonthTotalBudget(month)
        }


}