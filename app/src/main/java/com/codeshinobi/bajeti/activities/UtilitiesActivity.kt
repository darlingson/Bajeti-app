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
import com.codeshinobi.bajeti.Models.UtilitiesEntity
import com.codeshinobi.bajeti.ViewModels.OtherExpensesViewModel
import com.codeshinobi.bajeti.ViewModels.UtilitiesViewModel
import com.codeshinobi.bajeti.activities.ui.theme.BajetiTheme

class UtilitiesActivity : ComponentActivity() {
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
                        val viewModel: UtilitiesViewModel = viewModel(
                            it,
                            "UtilitiesViewModel",
                            UtilitiesViewModelFactory(
                                LocalContext.current.applicationContext
                                        as Application
                            )
                        )
                        UtilityExpensesScreenSetup(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun UtilityExpensesScreenSetup(viewModel: UtilitiesViewModel) {

    val allExpenses by viewModel.allExpenses.observeAsState(listOf())
    val searchResults by viewModel.searchResults.observeAsState(listOf())

    UtilitiesMainScreen(
        allExpenses = allExpenses,
        searchResults = searchResults,
        viewModel = viewModel
    )
}

@Composable
fun UtilitiesMainScreen(
    allExpenses: List<UtilitiesEntity>,
    searchResults: List<UtilitiesEntity>,
    viewModel: UtilitiesViewModel
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
        UtilitiesCustomTextField(
            title = "Expense Name",
            textState = ExpenseName,
            onTextChange = onExpenseTextChange,
            keyboardType = KeyboardType.Text
        )
        UtilitiesCustomTextField(
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
                    viewModel.insertUtilitiesExpense(
                        UtilitiesEntity (
                            name = ExpenseName,
                            amount = ExpenseAmount.toInt(),
                            date = "today",
                            type = "Utilities"
                        )
                    )
                    searching = false
                }
            }) {
                Text("Add")
            }

            Button(onClick = {
                searching = true
                viewModel.findUtilitiesExpense(ExpenseName)
            }) {
                Text("Search")
            }

            Button(onClick = {
                searching = false
                viewModel.deleteUtilitiesExpense(ExpenseName)
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
                UtilitiesCustomTitleRow(head1 = "ID", head2 = "Expense",head3="Amount")
            }

            items(allExpenses.size) { expense ->
                allExpenses[expense].name?.let {name->
                    allExpenses[expense].amount?.let { amount ->
                        UtilitiesCustomExpenseRow(
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
fun UtilitiesCustomTitleRow(head1: String, head2: String, head3: String) {
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
fun UtilitiesCustomExpenseRow(id: Int, name: String, amount: Int) {
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
fun UtilitiesCustomTextField(
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
fun UtilitiesCustomMainContent(name: String, modifier: Modifier = Modifier) {
    Column() {
        UtilitiesCustomTitleRow(head1 = "ID", head2 = "Name",  head3 = "Amount")
        UtilitiesCustomTextField(title = "ID", textState = "1", onTextChange = {}, keyboardType = KeyboardType.Number)
    }
}


@Composable
fun Greeting3(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    BajetiTheme {
        Greeting3("Android")
    }
}

class UtilitiesViewModelFactory(val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UtilitiesViewModel(application) as T
    }
}