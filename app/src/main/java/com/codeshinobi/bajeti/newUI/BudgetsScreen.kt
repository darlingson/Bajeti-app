package com.codeshinobi.bajeti.newUI

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codeshinobi.bajeti.newUI.Entities.Budget
import com.codeshinobi.bajeti.newUI.Entities.Expense
import com.codeshinobi.bajeti.newUI.Entities.SpendBudget
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

    val spendBudgets = viewModel.allSpendBudgets.observeAsState(emptyList())

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
            items(spendBudgets.value.filter {
                it.spendCategory.contains(
                    searchText,
                    ignoreCase = true
                )
            }) { spendBudget ->
                SpendBudgetListItem(
                    spendBudget = spendBudget,
                    onEditClicked = {/* TODO */ },
                    onDeleteClicked = {/* TODO */ }
                )
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
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var allSpendBudgets = viewModel.allSpendBudgets.observeAsState(listOf()).value

    val showDialogToDelete = remember { mutableStateOf(false) }
    val showDialogToEdit = remember { mutableStateOf(false) }
    var spendBudgetToDelete by remember { mutableStateOf<SpendBudget?>(null) }
    var spendBudgetToEdit by remember { mutableStateOf<SpendBudget?>(null) }

    if (showDialogToDelete.value) {
        DeleteSpendBudgetDialog(
            budget = spendBudgetToDelete!!,
            onDeleteClicked = { viewModel.delete(it) },
            onDismiss = { showDialogToDelete.value = false }
        )
    }

    // Edit Expense Dialog
    if (showDialogToEdit.value) {
        EditSpendBudgetDialog(
            budget = spendBudgetToEdit!!,
            onEditClicked = { viewModel.update(it) },
            onDismiss = { showDialogToEdit.value = false },
        )
    }
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
                items(allSpendBudgets.filter {
                    it.monthNumber == (currentMonth + 1) && it.year == currentYear && it.spendCategory.contains(
                        searchText,
                        ignoreCase = true
                    )
                }) { spendBudget ->
                    SpendBudgetListItem(
                        spendBudget = spendBudget,
                        onEditClicked = {
                            showDialogToEdit.value = true
                            spendBudgetToEdit = spendBudget
                        },
                        onDeleteClicked = {
                            showDialogToDelete.value = true
                            spendBudgetToDelete = spendBudget
                        }
                    )
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
                        AddSpendBudgetForm(viewModel)
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
@Preview
@Composable
fun SpendBudgetListItemCard() {
    SpendBudgetListItem(
        spendBudget = SpendBudget(
            spendCategory = "Food",
            amount = 300.0,
            monthNumber = 1,
            year = 2022,
            monthName = "January"
        ) ,
        onDeleteClicked = { /*TODO*/ },
        onEditClicked = { /*TODO*/ })
}
@Composable
fun SpendBudgetListItem(
    spendBudget: SpendBudget,
    onDeleteClicked: () -> Unit,
    onEditClicked: () -> Unit) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
            Column(
                modifier = Modifier
                    .weight(0.9f)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Category: ${spendBudget.spendCategory}",
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
                Column(
                    modifier = Modifier
                        .weight(0.1f)
                        .padding(16.dp)
                ) {
                    IconButton(
                        onClick = { onEditClicked() },
                        modifier = Modifier.requiredSize(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.Gray,
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    IconButton(
                        onClick = { onDeleteClicked() },
                        modifier = Modifier.requiredSize(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Gray,
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyBudgetsTab(viewModel: BudgetViewModel) {
    var searchText by remember { mutableStateOf("") }
    var budgets = viewModel.allSpendBudgets.observeAsState(emptyList())
    var monthlyBudgets = viewModel.currentMonthSpendBudgets.observeAsState(emptyList())
    var allBudgets = viewModel.allBudgets.observeAsState(emptyList())
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true},
                modifier = Modifier
                    .padding(top = 16.dp),
                content = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add") }
            )
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
            label = { Text("Search Budgets") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // List of Budgets
        LazyColumn(modifier = Modifier.padding(it)) {
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            items(allBudgets.value.filter {
                it.month_name.contains(
                    searchText,
                    ignoreCase = true
                )
            }) { budget ->
                if (budget.month_number != currentMonth || budget.year != currentYear) {
                    BudgetListItem(budget = budget)
                }
            }
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
                        Text("Add Monthly Budget")
                    }
                    AddBudgetForm(viewModel)
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
fun AddSpendBudgetForm(viewModel: BudgetViewModel) {
    val months = listOf(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    )
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var month_name by remember { mutableStateOf(months[0]) }
    var month_number by remember { mutableStateOf(1) }
    var year by remember { mutableStateOf("") }
    val yearSDF = SimpleDateFormat("yyyy", Locale.getDefault())
    year = yearSDF.format(Calendar.getInstance().time)


    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
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
            Box(modifier = Modifier.padding()){
                TextButton(
                    onClick = { expanded = true },
                    modifier = Modifier.padding(),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Text("Month: $month_name", color = Color.Black)
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
                                    month_number = (index + 1)
                                    expanded = false
                                },
                                text = { Text(month) }
                            )
                        }
                    }
                )
            }
            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Year") },
                modifier = Modifier.padding()
            )
            ElevatedButton(
                onClick = {
                    viewModel.insert(
                        SpendBudget(
                        amount = amount.toDouble(),
                        spendCategory = category,
                        monthName = month_name,
                        monthNumber = month_number,
                        year = year.toInt()
                    )
                    )
                    amount = ""
                    category = ""
                    month_name = months[0]
                    month_number = 1
                }
            ) {
                Text("Save")
            }
        }
    }
}
@Composable
fun AddBudgetForm(viewModel: BudgetViewModel) {
    val months = listOf(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    )
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var month_name by remember { mutableStateOf(months[0]) }
    var month_number by remember { mutableStateOf(1) }
    var year by remember { mutableStateOf("") }
    val yearSDF = SimpleDateFormat("yyyy", Locale.getDefault())
    year = yearSDF.format(Calendar.getInstance().time)


    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.padding()
            )
            Box(modifier = Modifier.padding()){
                TextButton(
                    onClick = { expanded = true },
                    modifier = Modifier.padding(),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Text("Month: $month_name", color = Color.Black)
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
                                    month_number = (index + 1)
                                    expanded = false
                                },
                                text = { Text(month) }
                            )
                        }
                    }
                )
            }
            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Year") },
                modifier = Modifier.padding()
            )
            ElevatedButton(
                onClick = {
                    viewModel.insert(
                        Budget(
                            amount = amount.toDouble(),
                            month_name = month_name,
                            month_number =  month_number,
                            year = year.toInt()
                        )
                    )
                    amount = ""
                    category = ""
                    month_name = months[0]
                    month_number = 1
                }
            ) {
                Text("Save")
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
            Text(text = budget.month_name, style = MaterialTheme.typography.bodySmall)
            Text(text = "Amount: ${budget.amount}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Year: ${budget.year}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
@Composable
fun DeleteSpendBudgetDialog(
    budget: SpendBudget,
    onDismiss: () -> Unit,
    onDeleteClicked: (SpendBudget) -> Unit,
    ){
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Confirm Deletion") },
        confirmButton = {
            Button(
                onClick = {
                    onDeleteClicked(budget)
                    onDismiss()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() }
            ) {
                Text("Cancel")
            }
        },
        text = {
            Text("Are you sure you want to delete this budget?")
        }
    )
}
@Composable
fun EditSpendBudgetDialog(
    budget: SpendBudget,
    onDismiss: () -> Unit,
    onEditClicked: (SpendBudget) -> Unit,
    ){
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Confirm Deletion") },
        confirmButton = {
            Button(
                onClick = {
                    onEditClicked(budget)
                    onDismiss()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() }
            ) {
                Text("Cancel")
            }
        },
        text = {
            Text("Are you sure you want to edit this budget?")
        }
    )
}


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