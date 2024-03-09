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

data class Budget(val name: String, val amount: Double, val category: String, val month: Date)

@Composable
fun BudgetsScreen() {
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
            val currentMonth = Calendar.getInstance().time
            item {
                BudgetListItem(Budget("This Month's Budget", 1000.0, "General", currentMonth))
            }
            items(budgets.filter { it.name.contains(searchText, ignoreCase = true) }) { budget ->
                if (budget.month != currentMonth) {
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
            Text(text = budget.name, style = MaterialTheme.typography.bodySmall)
            Text(text = "Amount: ${budget.amount}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Category: ${budget.category}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Month: ${SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(budget.month)}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

// Helper function to generate sample budgets
fun getSampleBudgets(): List<Budget> {
    return listOf(
        Budget("Groceries", 300.0, "Food", Date()),
        Budget("Clothing", 150.0, "Shopping", Date()),
        Budget("Utilities", 200.0, "Bills", Date()),
        Budget("This Month's Budget", 1000.0, "General", Calendar.getInstance().time),
        // Add more sample budgets as needed
    )
}

@Preview
@Composable
fun PreviewBudgetsScreen() {
    BudgetsScreen()
}