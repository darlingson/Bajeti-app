package com.codeshinobi.bajeti.newUI

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codeshinobi.bajeti.newUI.Entities.Expense
import com.codeshinobi.bajeti.newUI.ViewModels.BudgetViewModel
import com.codeshinobi.bajeti.ui.theme.BajetiTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(viewModel: BudgetViewModel) {
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
fun AddExpenseForm(viewModel: BudgetViewModel) {
    //getting today's date incase the expense was incurred today
    var expenseDatefroPicker by remember { mutableStateOf(Calendar.getInstance()) }
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    val todayDate = sdf.format(Date())

    var expenseDate by remember { mutableStateOf(todayDate) }
    var expenseName by remember { mutableStateOf("") }
    var expenseQuantity by remember { mutableStateOf(1) }
    var expenseAmount by remember { mutableStateOf("") }
    var expenseCategory by remember { mutableStateOf("") }

    val mContext = LocalContext.current
    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val mCalendar = Calendar.getInstance()
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    val mDate = remember { mutableStateOf("") }
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
        }, mYear, mMonth, mDay
    )
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
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "${mDate.value}", fontSize = 30.sp, textAlign = TextAlign.Center)
                Button(onClick = {
                    mDatePickerDialog.show()
                }, colors = ButtonDefaults.buttonColors(containerColor = Color(0XFF0F9D58)) ) {
                    Text(text = "Open Date Picker", color = Color.White)
                }
            }
            OutlinedTextField(
                value = expenseDate,
                onValueChange = { expenseDate = it },
                label = { Text("Expense Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = expenseName,
                onValueChange = { expenseName = it },
                label = { Text("Expense Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = expenseQuantity.toString(),
                onValueChange = { expenseQuantity = it.toInt() },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Expense Quantity") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = expenseAmount,
                onValueChange = { expenseAmount = it },
                label = { Text("Expense Amount") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = expenseCategory,
                onValueChange = { expenseCategory = it },
                label = { Text("Expense Category") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    viewModel.insert(
                        Expense(
                            name = expenseName,
                            quantity = expenseQuantity.toFloat(),
                            amount = expenseAmount.toDouble(),
                            category = expenseCategory,
                            date = expenseDate,
                            description = "",
                            monthNumber = 1,
                            weekNumber = 1,
                            year = 2023,
                            month = "January",
                        )
                    )
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
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
//        ExpensesScreen()
        ExpensesScreenTabScreen()
    }
}
//@Preview
//@Composable
//fun AddExpenseFormPreview() {
//    BajetiTheme {
//        AddExpenseForm(viewModel)
//    }
//
//}
data class PlaceHolderExpense(val name: String, val amount: Double, val category: String, val date: Date)