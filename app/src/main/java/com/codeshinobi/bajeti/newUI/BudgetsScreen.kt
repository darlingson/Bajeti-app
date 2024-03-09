package com.codeshinobi.bajeti.newUI

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class Budget(val amount: Double, val monthName: String, val monthNumber: Int, val year: Int)

@Composable
fun BudgetsScreen() {
    BudgetsScreenTabScreen()
}
@Composable
fun BudgetsScreenTabScreen(){
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Total Budgets", "Current Spend Budget", "Previous Spend Budgets")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        when (tabIndex) {
            0 -> MonthlyBudgetsTab()
            1 -> CurrentSpendBudgetTab()
            2 -> PreviousSpendBudgetsTab()
        }
    }
}
@Composable
fun PreviousSpendBudgetsTab(){
    Text(text = "Previous spend")
}
@Composable
fun CurrentSpendBudgetTab(){
    Text(text = "Current spend")
}
@Composable
fun MonthlyBudgetsTab(){
    var searchText by remember { mutableStateOf("") }

    // Replace this with your actual data fetching logic or use sample data
    val budgets = getSampleBudgets()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Budgets") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // List of Budgets
        LazyColumn {
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            item {
                BudgetListItem(
                    Budget(
                        1000.0,
                        SimpleDateFormat("MMMM", Locale.getDefault()).format(Calendar.getInstance().time),
                        currentMonth,
                        currentYear
                    )
                )
            }
            items(budgets.filter { it.monthName.contains(searchText, ignoreCase = true) }) { budget ->
                if (budget.monthNumber != currentMonth || budget.year != currentYear) {
                    BudgetListItem(budget = budget)
                }
            }
        }
    }
}
@Composable
fun BudgetListItem(budget: Budget) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = budget.monthName, style = MaterialTheme.typography.bodySmall)
            Text(text = "Amount: ${budget.amount}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Year: ${budget.year}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// Helper function to generate sample budgets
fun getSampleBudgets(): List<Budget> {
    return listOf(
        Budget(1000.0, "January", 0, 2023),
        Budget(2000.0, "February", 1, 2023),
        Budget(3000.0, "March", 2, 2023),
        Budget(4000.0, "April", 3, 2023),
        Budget(5000.0, "May", 4, 2023),
        Budget(6000.0, "June", 5, 2023),
        Budget(7000.0, "July", 6, 2023),
        Budget(8000.0, "August", 7, 2023),
        Budget(9000.0, "September", 8, 2023),
        Budget(10000.0, "October", 9, 2023),
        Budget(11000.0, "November", 10, 2023),
        Budget(12000.0, "December", 11, 2023),
        Budget(12000.0, "January", 0, 2024),
        Budget(12000.0, "February", 1, 2024),
        Budget(12000.0, "March", 2, 2024),
    )
}

@Preview
@Composable
fun PreviewBudgetsScreen() {
    BudgetsScreen()
}