package com.codeshinobi.bajeti.newUI

import android.app.DatePickerDialog
import android.net.Uri
import android.util.Log
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
//import com.codeshinobi.bajeti.Manifest
import com.codeshinobi.bajeti.NoPermissionScreen
import com.codeshinobi.bajeti.newUI.Entities.Expense
import com.codeshinobi.bajeti.newUI.ViewModels.BudgetViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Objects
import android.Manifest
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import coil.compose.rememberImagePainter
import com.codeshinobi.bajeti.R
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(viewModel: BudgetViewModel) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    Scaffold(
        contentColor = MaterialTheme.colorScheme.tertiary,
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
        ExpensesScreenTabScreen(viewModel)

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                modifier = Modifier.fillMaxHeight()
            ) {
                var modaltabIndex by remember { mutableStateOf(0) }
                val modaltabs= listOf("form", "camera",)

                    TabRow(selectedTabIndex = modaltabIndex) {
                        modaltabs.forEachIndexed { index, title ->
                            Tab(text = { Text(title) },
                                selected = modaltabIndex == index,
                                onClick = { modaltabIndex = index }
                            )
                        }
                    }
                    Button(onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }) {
                        Text("Close")
                    }
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(contentPadding))
                {
                    when (modaltabIndex) {
                        0 -> AddExpenseForm(viewModel)
                        1 -> CameraComposable()
                    }
                }

//                Column(
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .fillMaxWidth()
//                        .padding(contentPadding)
//                        .background(color = MaterialTheme.colorScheme.tertiary)
//                ) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.Center
//                    ){
//                        Text("Add expense")
//                    }
//                    AddExpenseForm(viewModel)
//                    Button(onClick = {
//                        scope.launch { sheetState.hide() }.invokeOnCompletion {
//                            if (!sheetState.isVisible) {
//                                showBottomSheet = false
//                            }
//                        }
//                    }) {
//                        Text("Close")
//                    }
//                }
            }
        }
    }
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraComposable() {
    val cameraPermissionState:PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    CameraPermissions(
        hasPermission = cameraPermissionState.status.isGranted,
        onRequestPermission = cameraPermissionState::launchPermissionRequest)
}
@Composable
fun CameraPermissions(hasPermission:Boolean,onRequestPermission:()->Unit){
    if (hasPermission) {
        CameraScreen()
    }
    else{
        NoPermissionScreen(onRequestPermission)
    }
}
@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider",
        file
    )
    var captureImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
    captureImageUri = uri
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ){
        if (it){
            cameraLauncher.launch(uri)
        }
        else{

        }
    }
    Column {
        Button(onClick = {
            val permissionCheckResult =
                ContextCompat.checkSelfPermission(context,Manifest.permission.CAMERA)
            if (permissionCheckResult == android.content.pm.PackageManager.PERMISSION_GRANTED){
                cameraLauncher.launch(uri)
            }
            else{
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }){
            Text("Camera")
        }
    }

    if (captureImageUri.path?.isNotEmpty() == true) {
        Image(
            modifier = Modifier.padding(16.dp,8.dp),
            painter = rememberImagePainter(captureImageUri),
            contentDescription = null)
    }
    else{
        Image(
            modifier = Modifier.padding(16.dp,8.dp),
            painter = painterResource(id = R.drawable.baseline_image_24),
            contentDescription = null)
    }

}
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}
@Composable
fun ExpensesScreenTabScreen(viewModel: BudgetViewModel) {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Current", "All",)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.tertiary)
    ) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        when (tabIndex) {
            0 -> CurrentMonthExpensesTab(viewModel)
            1 -> AllExpensesTab(viewModel)
        }
    }
}
@Composable
fun CurrentMonthExpensesTab(viewModel: BudgetViewModel) {
    Column {
        Text("Current Month Expenses")
        ExpenseListScreen(viewModel, true)
    }
}
@Composable
fun AllExpensesTab(viewModel: BudgetViewModel) {
    Column {
        ExpenseListScreen(viewModel, false)
    }
}
@Composable
fun SearchExpenses(viewModel: BudgetViewModel) {
    Text("Search Expenses")
}
@Composable
fun AddExpenseForm(viewModel: BudgetViewModel) {
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    val todayDate = sdf.format(Date())

    var expenseDate by remember { mutableStateOf(todayDate) }
    var expenseName by remember { mutableStateOf("") }
    var expenseDescription by remember { mutableStateOf("") }
    var expenseQuantity by remember { mutableStateOf("1") }
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

    var monthName by remember { mutableStateOf(SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())) }
    var monthNumber by remember { mutableStateOf(mMonth) }
    var weekNumber by remember { mutableStateOf(0) }
    var year by remember { mutableStateOf(0) }

    val mDate = remember { mutableStateOf(expenseDate) }

    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, mYear)
                set(Calendar.MONTH, mMonth)
                set(Calendar.DAY_OF_MONTH, mDayOfMonth)

                monthNumber = mMonth
            }

            monthName = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
            monthNumber = mMonth + 1
            weekNumber = calendar.get(Calendar.WEEK_OF_YEAR)
            year = mYear

            mDate.value = "$mDayOfMonth/$monthNumber/$mYear"
            expenseDate = "$mDayOfMonth/$monthNumber/$mYear"

            Log.d("DatePicker", "Selected Date: $mDayOfMonth/$monthNumber/$mYear")
            Log.d("DatePicker", "Month Name: $monthName")
            Log.d("DatePicker", "Month Number: $monthNumber")
            Log.d("DatePicker", "Week Number: $weekNumber")

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
                OutlinedTextField(
                    readOnly = true,
                    value = expenseDate,
                    onValueChange = { expenseDate = it },
                    label = { Text("Expense Date") },
                    modifier = Modifier
                        .weight(0.6f)
                        .padding(bottom = 8.dp)
                )
                Button(onClick = {
                    mDatePickerDialog.show()
                }, colors = ButtonDefaults.buttonColors(containerColor = Color(0XFF0F9D58)) ) {
                    Text(text = "Pick Date", color = Color.White)
                }
            }
            OutlinedTextField(
                value = expenseName,
                onValueChange = { expenseName = it },
                label = { Text("Expense Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value =  expenseDescription,
                onValueChange = { expenseDescription = it },
                label = { Text("Expense Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = expenseQuantity.toString(),
                onValueChange = { expenseQuantity = it },
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
                            description = expenseDescription,
                            monthNumber = monthNumber,
                            weekNumber = weekNumber,
                            year = year,
                            month = monthName,
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
fun ExpenseListScreen(viewModel: BudgetViewModel,currentmonth:Boolean) {
    var searchText by remember { mutableStateOf("") }
    val expenses by viewModel.allExpenses.observeAsState(emptyList())
    val currentMonthNumber = Calendar.getInstance().get(Calendar.MONTH) + 1

    val showDialogToDelete = remember { mutableStateOf(false) }
    val showDialogToEdit = remember { mutableStateOf(false) }
    var expenseToDelete by remember { mutableStateOf<Expense?>(null) }
    var expenseToEdit by remember { mutableStateOf<Expense?>(null) }



    // Delete Expense Dialog
    if (showDialogToDelete.value) {
        DeleteExpenseDialog(
            expense = expenseToDelete!!,
            onDeleteClicked = { viewModel.delete(it) },
            onDismiss = { showDialogToDelete.value = false }
        )
    }

    // Edit Expense Dialog
    if (showDialogToEdit.value) {
        EditExpenseDialog(
            expense = expenseToEdit!!,
            onEditClicked = { viewModel.update(it) },
            onDismiss = { showDialogToEdit.value = false },
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Expenses") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        LazyColumn(
//            modifier = Modifier.background(color = MaterialTheme.colorScheme.primary)
        )
        {
            items(
                expenses
                    .filter { it.name.contains(searchText, ignoreCase = true) }
                    .filter { expense ->
                        if (currentmonth) {
                            Log.d("condition","month number : ${currentMonthNumber}, expense month number: ${expense.monthNumber}")
                            expense.monthNumber == currentMonthNumber
                        } else {
                            Log.d("condition","month number : ${currentMonthNumber}, expense month number: ${expense.monthNumber}")
                            true
                        }
                    }
            ) { expense ->
                ExpenseListItem(
                    expense = expense,
                    onDeleteClicked = {
                        expenseToDelete = expense
                        showDialogToDelete.value = true
                    },
                    onEditClicked = {
                        expenseToEdit = expense
                        showDialogToEdit.value = true
                    }
                )
            }
        }
    }
}

@Composable
fun ExpenseListItem(
    expense: Expense,
    onDeleteClicked: () -> Unit,
    onEditClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(0.9f)
                    .padding(16.dp)
            ) {
                Text(text = expense.name, style = MaterialTheme.typography.bodySmall)
                Text(text = "Amount: ${expense.amount}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Category: ${expense.category}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Date: ${expense.date}", style = MaterialTheme.typography.bodyLarge)
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
@Composable
fun EditExpenseDialog(
    expense: Expense,
    onDismiss: () -> Unit,
    onEditClicked: (Expense) -> Unit,
) {
    var editedExpenseName by remember { mutableStateOf(expense.name) }
    var editedExpenseAmount by remember { mutableStateOf(expense.amount) }
    var editedExpenseCategory by remember { mutableStateOf(expense.category) }
    var editedExpenseDate by remember { mutableStateOf(expense.date) }
    var editedQuantity by remember { mutableStateOf(expense.quantity) }
    var editedDescription by remember { mutableStateOf(expense.description) }
    var editedMonthNumber by remember { mutableStateOf(expense.monthNumber) }
    var editedWeekNumber by remember { mutableStateOf(expense.weekNumber) }
    var editedYear by remember { mutableStateOf(expense.year) }
    var editedMonth by remember { mutableStateOf(expense.month) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Edit Expense") },
        confirmButton = {
            Button(
                onClick = {
//                    val editedExpense = Expense(
//                        name = editedExpenseName,
//                        amount = editedExpenseAmount,
//                        category = editedExpenseCategory,
//                        date = editedExpenseDate,
//                        quantity = editedQuantity,
//                        description = editedDescription,
//                        monthNumber = editedMonthNumber,
//                        weekNumber = editedWeekNumber,
//                        year = editedYear,
//                        month = editedMonth
//                    )
                    onEditClicked(
                        Expense(
                            id = expense.id,
                            name = editedExpenseName,
                            amount = editedExpenseAmount,
                            category = editedExpenseCategory,
                            date = editedExpenseDate,
                            quantity = editedQuantity,
                            description = editedDescription,
                            monthNumber = editedMonthNumber,
                            weekNumber = editedWeekNumber,
                            year = editedYear,
                            month = editedMonth
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("Save")
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
            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                TextField(
                    value = editedExpenseName,
                    onValueChange = { editedExpenseName = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = editedExpenseAmount.toString(),
                    onValueChange = {editedExpenseAmount = it.toDouble()},
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = editedExpenseCategory,
                    onValueChange = { editedExpenseCategory = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = editedExpenseDate,
                    onValueChange = { editedExpenseDate = it },
                    label = { Text("Date") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onDismiss() })
                )
            }
        }
    )
}

@Composable
fun DeleteExpenseDialog(
    expense: Expense,
    onDismiss: () -> Unit,
    onDeleteClicked: (Expense) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Confirm Deletion") },
        confirmButton = {
            Button(
                onClick = {
                    onDeleteClicked(expense)
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
            Text("Are you sure you want to delete this expense?")
        }
    )
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
//@Preview
//@Composable
//fun ExpensesScreenPreview() {
//    BajetiTheme {
////        ExpensesScreen()
//        ExpensesScreenTabScreen(viewModel)
//    }
//}
//@Preview
//@Composable
//fun AddExpenseFormPreview() {
//    BajetiTheme {
//        AddExpenseForm(viewModel)
//    }
//
//}
data class PlaceHolderExpense(val name: String, val amount: Double, val category: String, val date: Date)