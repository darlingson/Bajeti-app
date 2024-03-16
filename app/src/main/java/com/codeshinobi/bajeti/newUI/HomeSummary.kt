package com.codeshinobi.bajeti.newUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codeshinobi.bajeti.newUI.ViewModels.BudgetViewModel

@Composable
fun HomeSummary(viewModel: BudgetViewModel) {
//    Text(text = "Summary")
    Column(
        modifier = Modifier
            .fillMaxSize()
//            .verticalScroll(rememberScrol lState())
    ) {
        SummaryBudgetCard(viewModel)
        Divider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )
        MonthlyExpensesSummaryList(viewModel)
    }
}

@Composable
fun SummaryBudgetCard(budgetVM: BudgetViewModel) {
    val budgetViewModel: BudgetViewModel =  budgetVM

    val allExpenses by budgetViewModel.allExpenses.observeAsState(emptyList())

    val currentMonthExpenses by budgetViewModel.currentMonthExpenses.observeAsState(emptyList())
    val budget by budgetViewModel.allBudgets.observeAsState(initial = emptyList())
    val expenses by budgetViewModel.allExpenses.observeAsState(initial = emptyList())

    val totalBudget = budget.sumOf { it.amount }
    val totalExpenses = expenses.sumOf { it.amount }
    val spendPercentage = (totalExpenses / totalBudget) * 100
    val backgroundColor = when {
        spendPercentage < 70 -> Color.Green
        spendPercentage in 71.0..99.0 -> Color.Magenta
        else -> Color.Red
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Budget : K${totalBudget.toInt()}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Current Spend : K${totalExpenses.toInt()}",
                color = backgroundColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun MonthlyExpensesSummaryList(viewModel: BudgetViewModel) {
    val expenses by viewModel.allExpenses.observeAsState(emptyList())
    var searchText by remember { mutableStateOf("") }
//    Column {
//        ExpenseCard(name = "Spaghetti", amount = "1,200", category = "Food", date = "2023-05-01")
//        ExpenseCard(name = "Spaghetti", amount = "1,200", category = "Food", date = "2023-05-01")
//        ExpenseCard(name = "Spaghetti", amount = "1,200", category = "Food", date = "2023-05-01")
//        ExpenseCard(name = "Spaghetti", amount = "1,200", category = "Food", date = "2023-05-01")
//        ExpenseCard(name = "Spaghetti", amount = "1,200", category = "Food", date = "2023-05-01")
//        ExpenseCard(name = "Spaghetti", amount = "1,200", category = "Food", date = "2023-05-01")
//        ExpenseCard(name = "Spaghetti", amount = "1,200", category = "Food", date = "2023-05-01")
//    }

        Text(text = "Expenses Summary")
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Expenses") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
    LazyColumn {
        items(expenses.filter { it.name.contains(searchText, ignoreCase = true) }) { expense ->
            ExpenseCard(
                name = expense.name,
                amount = expense.amount.toString(),
                category = expense.category,
                date = expense.date
            )
        }
    }
}

@Composable
fun ExpenseCard(
    name: String,
    amount: String,
    category: String,
    date: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // First Row: Display Amount
            Text(
                text = "$amount MWK",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Second Row: Display Date, Name, and Category
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    text = "$name - $category",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomeSummaryPreview() {
//    HomeSummary(viewModel)
//}