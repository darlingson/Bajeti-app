package com.codeshinobi.bajeti.activities

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codeshinobi.bajeti.Models.OtherExpensesEntity
import com.codeshinobi.bajeti.ViewModels.OtherExpensesViewModel
import com.codeshinobi.bajeti.activities.ui.theme.BajetiTheme

class OtherExpensesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BajetiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val owner = LocalViewModelStoreOwner.current

                    owner?.let {
                        val viewModel: OtherExpensesViewModel = viewModel(
                            it,
                            "OtherExpensesViewModel",
                            OtherExpensesViewModelFactory(
                                LocalContext.current.applicationContext
                                        as Application
                            )
                        )

                        OtherExpensesScreenSetup(viewModel)
                    }
                }
            }
        }
    }
}
@Composable
fun OtherExpensesScreenSetup(viewModel: OtherExpensesViewModel) {

    val allExpenses by viewModel.allExpenses.observeAsState(listOf())
    val searchResults by viewModel.searchResults.observeAsState(listOf())

    OtherUtilitiesMainScreen(
        allExpenses = allExpenses,
        searchResults = searchResults,
        viewModel = viewModel
    )
}

@Composable
fun OtherUtilitiesMainScreen(
    allExpenses: List<OtherExpensesEntity>,
    searchResults: List<OtherExpensesEntity>,
    viewModel: OtherExpensesViewModel
) {
    var ExpenseName by remember { mutableStateOf("") }
    var ExpenseCategory by remember { mutableStateOf("") }
    var ExpenseAmount by remember { mutableStateOf("") }
    var searching by remember { mutableStateOf(false) }

    val onExpenseTextChange = { text : String ->
        ExpenseName = text
    }
    val onAmountTextChange = { text : String ->
        ExpenseAmount = text
    }
    val onCategoryTextChange = { text : String ->
        ExpenseCategory = text
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OtherExpensesCustomTextField(
            title = "Expense Name",
            textState = ExpenseName,
            onTextChange = onExpenseTextChange,
            keyboardType = KeyboardType.Text
        )
        OtherExpensesCustomTextField(
            title = "Expense Amount",
            textState = ExpenseAmount,
            onTextChange = onAmountTextChange,
            keyboardType = KeyboardType.Number
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Button(onClick = {
                if (ExpenseAmount.isNotEmpty()) {
                    viewModel.insertOtherExpense(
                        OtherExpensesEntity (
                            name = ExpenseName,
                            amount = ExpenseAmount.toInt()

                        )
                    )
                    searching = false
                }
            }) {
                Text("Add")
            }

            Button(onClick = {
                searching = true
                viewModel.findOtherExpense(ExpenseName)
            }) {
                Text("Search")
            }

            Button(onClick = {
                searching = false
                viewModel.deleteOtherExpense(ExpenseName)
            }) {
                Text("Delete")
            }

            Button(onClick = {
                searching = false
                ExpenseName = ""
                ExpenseAmount = ""
            }) {
                Text("Clear")
            }
        }
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            val list = if (searching) searchResults else allExpenses

            item {
                OtherExpensesCustomTitleRow(head1 = "ID", head2 = "Expense",head3="Amount")
            }

            items(allExpenses.size) { expense ->
                allExpenses[expense].name?.let {name->
                    allExpenses[expense].amount?.let { amount ->
                        OtherExpensesCustomExpenseRow(
                            id = allExpenses[expense].id,
                            name = name,
                            amount = amount
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun OtherExpensesCustomTitleRow(head1: String, head2: String, head3: String) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(head1, color = Color.White,
            modifier = Modifier
                .weight(0.1f))
        Text(head2, color = Color.White,
            modifier = Modifier
                .weight(0.2f))
        Text(head3, color = Color.White,
            modifier = Modifier.weight(0.2f))
    }
}

@Composable
fun OtherExpensesCustomExpenseRow(id: Int, name: String, amount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(id.toString(), modifier = Modifier
            .weight(0.1f))
        Text(name, modifier = Modifier.weight(0.2f))
        Text(amount.toString(), modifier = Modifier.weight(0.2f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherExpensesCustomTextField(
    title: String,
    textState: String,
    onTextChange: (String) -> Unit,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = textState,
        onValueChange = { onTextChange(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        singleLine = true,
        label = { Text(title)},
        modifier = Modifier.padding(10.dp),
        textStyle = TextStyle(fontWeight = FontWeight.Bold,
            fontSize = 30.sp)
    )
}
@Composable
fun OtherExpensesCustomMainContent(name: String, modifier: Modifier = Modifier) {
    Column() {
        OtherExpensesCustomTitleRow(head1 = "ID", head2 = "Name",  head3 = "Amount")
        OtherExpensesCustomTextField(title = "ID", textState = "1", onTextChange = {}, keyboardType = KeyboardType.Number)
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview5() {
    BajetiTheme {
        OtherExpensesCustomMainContent("Bajeti")
    }
}

class OtherExpensesViewModelFactory(val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OtherExpensesViewModel(application) as T
    }
}