package com.codeshinobi.bajeti.newUI

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codeshinobi.bajeti.ui.theme.BajetiTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add expense") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                onClick = {
                    showBottomSheet = true
                }
            )
        }
    ) { contentPadding ->
        // Screen content
        ExpensesScreenTabScreen()

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
                        .padding(contentPadding)
                ) {
                    Text("Add expense")
                    AddExpenseForm()
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
@Composable
fun ExpensesScreenTabScreen() {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Current", "All", "Search")

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
            0 -> CurrentMonthExpensesTab()
            1 -> AllExpensesTab()
            2 -> SearchExpenses()
        }
    }
}
@Composable
fun CurrentMonthExpensesTab() {
    Column {
        Text("Current Month Expenses")
        ExpenseListScreen()
    }
}
@Composable
fun AllExpensesTab() {
    Text("All Expenses")
}
@Composable
fun SearchExpenses() {
    Text("Search Expenses")
}
@Composable
fun AddExpenseForm() {
    //getting today's date incase the expense was incurred today
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    val todayDate = sdf.format(Date())

    var expenseDate by remember { mutableStateOf(todayDate) }
    var expenseName by remember { mutableStateOf("") }
    var expenseQuantity by remember { mutableStateOf(1) }
    var expenseAmount by remember { mutableStateOf("") }
    var expenseCategory by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Expense Date
            OutlinedTextField(
                value = expenseDate,
                onValueChange = { expenseDate = it },
                label = { Text("Expense Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            // Expense Name
            OutlinedTextField(
                value = expenseName,
                onValueChange = { expenseName = it },
                label = { Text("Expense Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            // Expense Quantity
            OutlinedTextField(
                value = expenseQuantity.toString(),
                onValueChange = { expenseQuantity = it.toInt() },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Expense Quantity") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            // Expense Amount
            OutlinedTextField(
                value = expenseAmount,
                onValueChange = { expenseAmount = it },
                label = { Text("Expense Amount") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Expense Category
            OutlinedTextField(
                value = expenseCategory,
                onValueChange = { expenseCategory = it },
                label = { Text("Expense Category") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Button to Add Expense (You can replace this with your desired action)
            Button(
                onClick = {
                    // Add expense logic goes here
                    // For example, you can send data to a ViewModel or perform some action
                    // based on the entered expense details.
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add Expense")
            }
        }
    }
}
@Composable
fun ExpenseListScreen() {
    var searchText by remember { mutableStateOf("") }

    // Replace this with your actual data fetching logic or use sample data
    val expenses = getSampleExpenses()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Expenses") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // List of Expenses
        LazyColumn {
            items(expenses.filter { it.name.contains(searchText, ignoreCase = true) }) { expense ->
                ExpenseListItem(expense = expense)
            }
        }
    }
}

@Composable
fun ExpenseListItem(expense: PlaceHolderExpense) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = expense.name, style = MaterialTheme.typography.bodySmall)
            Text(text = "Amount: ${expense.amount}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Category: ${expense.category}", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Date: ${SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(expense.date)}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

// Helper function to generate sample expenses
fun getSampleExpenses(): List<PlaceHolderExpense> {
    return listOf(
        PlaceHolderExpense("Groceries", 50.0, "Food", Date()),
        PlaceHolderExpense("Clothing", 30.0, "Shopping", Date()),
        PlaceHolderExpense("Utilities", 80.0, "Bills", Date()),
        // Add more sample expenses as needed
    )
}
@Preview
@Composable
fun ExpensesScreenPreview() {
    BajetiTheme {
        ExpensesScreen()
    }
}

data class PlaceHolderExpense(val name: String, val amount: Double, val category: String, val date: Date)