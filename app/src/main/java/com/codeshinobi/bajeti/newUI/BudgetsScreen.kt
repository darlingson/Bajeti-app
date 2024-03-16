package com.codeshinobi.bajeti.newUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codeshinobi.bajeti.newUI.ViewModels.BudgetViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun BudgetsScreen(viewModel: BudgetViewModel) {
    BudgetsScreenTabScreen(viewModel)
}

@Composable
fun BudgetsScreenTabScreen(viewModel: BudgetViewModel) {
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
            0 -> MonthlyBudgetsTab(viewModel)
            1 -> CurrentSpendBudgetTab(viewModel)
            2 -> PreviousSpendBudgetsTab(viewModel)
        }
    }
}

@Composable
fun PreviousSpendBudgetsTab(viewModel: BudgetViewModel) {
    var searchText by remember { mutableStateOf("") }

    val spendBudgets = getSampleSpendBudgets()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Spend Budgets") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // List of Spend Budgets
        LazyColumn {
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            items(spendBudgets.filter {
                it.category.contains(
                    searchText,
                    ignoreCase = true
                )
            }) { spendBudget ->
                SpendBudgetListItem(spendBudget = spendBudget)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentSpendBudgetTab(viewModel: BudgetViewModel) {
    var searchText by remember { mutableStateOf("") }
    var isModalOpen by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val spendBudgets = getSampleSpendBudgets()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search Spend Budgets") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // List of Spend Budgets
            LazyColumn {
                val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                items(spendBudgets.filter {
                    it.monthNumber == currentMonth && it.year == currentYear && it.category.contains(
                        searchText,
                        ignoreCase = true
                    )
                }) { spendBudget ->
                    SpendBudgetListItem(spendBudget = spendBudget)
                }
            }
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    // Sheet content
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .padding(it)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ){
                            Text("Add expense")
                        }
                        AddExpenseForm(viewModel)
                        Button(onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }) {
                            Text("Close")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SpendBudgetListItem(spendBudget: PlaceHolderSpendBudget) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Category: ${spendBudget.category}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Amount: ${spendBudget.amount}",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "Month: ${spendBudget.monthName} ${spendBudget.year}",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

fun getSampleSpendBudgets(): List<PlaceHolderSpendBudget> {
    return listOf(
        PlaceHolderSpendBudget("January", 0, 2022, "Food", 300.0),
        PlaceHolderSpendBudget("January", 0, 2022, "Shopping", 150.0),
        PlaceHolderSpendBudget("March", 2, 2024, "Bills", 200.0),
        PlaceHolderSpendBudget("March", 2, 2024, "Utilities", 30000.0),
        PlaceHolderSpendBudget("March", 2, 2024, "Bills", 50000.0),
        PlaceHolderSpendBudget("March", 2, 2024, "Food", 70000.0),
        PlaceHolderSpendBudget("March", 2, 2024, "Entertainment", 30000.0),
        PlaceHolderSpendBudget("March", 3, 2024, "Entertainment", 100.0),
        // Add more sample spend budgets as needed
    )
}

@Composable
fun MonthlyBudgetsTab(viewModel: BudgetViewModel) {
    var searchText by remember { mutableStateOf("") }

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
                    PlaceholderBudget(
                        1000.0,
                        SimpleDateFormat(
                            "MMMM",
                            Locale.getDefault()
                        ).format(Calendar.getInstance().time),
                        currentMonth,
                        currentYear
                    )
                )
            }
            items(budgets.filter {
                it.monthName.contains(
                    searchText,
                    ignoreCase = true
                )
            }) { budget ->
                if (budget.monthNumber != currentMonth || budget.year != currentYear) {
                    BudgetListItem(budget = budget)
                }
            }
        }
    }
}
@Composable
fun AddSpendBudgetForm(){
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var month_name by remember { mutableStateOf("") }
    var month_number by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    val yearSDF = SimpleDateFormat("yyyy", Locale.getDefault())
    year = yearSDF.format(Calendar.getInstance().time)

    val months = listOf(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    )
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.padding()
            )
            OutlinedTextField(
                value = category ,
                onValueChange ={category = it},
                label = { Text("Category") },
                modifier = Modifier.padding()
            )
            Box(modifier = Modifier.fillMaxWidth()){
                TextButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Month: $month_name")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        months.forEachIndexed() { index, month ->
                            DropdownMenuItem(
                                onClick = {
                                    month_name = month
                                    month_number = (index + 1).toString()
                                    expanded = false
                                },
                                text = { Text(month) }
                            )
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AddSpendBudgetPreview() {
    AddSpendBudgetForm()
}
@Composable
fun BudgetListItem(budget: PlaceholderBudget) {
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

fun getSampleBudgets(): List<PlaceholderBudget> {
    return listOf(
        PlaceholderBudget(1000.0, "January", 0, 2023),
        PlaceholderBudget(2000.0, "February", 1, 2023),
        PlaceholderBudget(3000.0, "March", 2, 2023),
        PlaceholderBudget(4000.0, "April", 3, 2023),
        PlaceholderBudget(5000.0, "May", 4, 2023),
        PlaceholderBudget(6000.0, "June", 5, 2023),
        PlaceholderBudget(7000.0, "July", 6, 2023),
        PlaceholderBudget(8000.0, "August", 7, 2023),
        PlaceholderBudget(9000.0, "September", 8, 2023),
        PlaceholderBudget(10000.0, "October", 9, 2023),
        PlaceholderBudget(11000.0, "November", 10, 2023),
        PlaceholderBudget(12000.0, "December", 11, 2023),
        PlaceholderBudget(12000.0, "January", 0, 2024),
        PlaceholderBudget(12000.0, "February", 1, 2024),
        PlaceholderBudget(12000.0, "March", 2, 2024),
    )
}

//@Preview
//@Composable
//fun PreviewBudgetsScreen() {
//    BudgetsScreen(viewModel)
//}
//
//@Preview
//@Composable
//fun PreviewCurrentSpendBudgetTab() {
//    CurrentSpendBudgetTab(viewModel)
//}
//
//@Preview
//@Composable
//fun PreviewPreviousSpendBudgetsTab() {
//    PreviousSpendBudgetsTab(viewModel)
//}


data class PlaceholderBudget(
    val amount: Double,
    val monthName: String,
    val monthNumber: Int,
    val year: Int
)

data class PlaceHolderSpendBudget(
    val monthName: String,
    val monthNumber: Int,
    val year: Int,
    val category: String,
    val amount: Double
)