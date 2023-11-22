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
import com.codeshinobi.bajeti.Models.ExpenseEntity
import com.codeshinobi.bajeti.ViewModels.ExpensesViewModel
import com.codeshinobi.bajeti.activities.ui.theme.BajetiTheme

class TransportExpeneseActivity : ComponentActivity() {
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
                        val viewModel: ExpensesViewModel = viewModel(
                            it,
                            "ExpensesViewModel",
                            ExpensesViewModelFactory(
                                LocalContext.current.applicationContext
                                        as Application
                            )
                        )

                        TransportScreenSetup(viewModel)
                    }
                }
            }
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview4() {
//    BajetiTheme {
//        Greeting3("Android")
//    }
//}

@Composable
fun TransportScreenSetup(viewModel: ExpensesViewModel) {

    val allExpenses by viewModel.allExpenses.observeAsState(listOf())
    val searchResults by viewModel.searchResults.observeAsState(listOf())

    TransportMainScreen(
        allExpenses = allExpenses,
        searchResults = searchResults,
        viewModel = viewModel
    )
}

@Composable
fun TransportMainScreen(
    allExpenses: List<ExpenseEntity>,
    searchResults: List<ExpenseEntity>,
    viewModel: ExpensesViewModel
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
        TransportCustomTextField(
            title = "Expense Name",
            textState = ExpenseName,
            onTextChange = onExpenseTextChange,
            keyboardType = KeyboardType.Text
        )
        TransportCustomTextField(
            title = "Expense Category",
            textState = ExpenseCategory,
            onTextChange = onCategoryTextChange,
            keyboardType = KeyboardType.Text
        )

        TransportCustomTextField(
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
                    viewModel.insertProduct(
                        ExpenseEntity(
                            name = ExpenseName,
                            Category = ExpenseCategory,
                            type = "Food",
                            amount = ExpenseAmount.toInt()

                        )
                    )
                    searching = false
                }
            }) {
                Text("Add Location")
            }

            Button(onClick = {
                searching = true
                viewModel.findProduct(ExpenseName)
            }) {
                Text("Search")
            }

            Button(onClick = {
                searching = false
                viewModel.deleteProduct(ExpenseName)
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
                TransportTitleRow(head1 = "ID", head2 = "Expense", head3 = "Category",head4="Amount")
            }

            items(allExpenses.size) { expense ->
                allExpenses[expense].name?.let {
                    allExpenses[expense].amount?.let { it1 ->
                        allExpenses[expense].Category?.let { it2 ->
                            TransportExpenseRow(id = allExpenses[expense].id, name = it,
                                category = it2, amount = it1
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun TransportTitleRow(head1: String, head2: String, head3: String,head4:String) {
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
        Text(head4, color = Color.White,
            modifier = Modifier.weight(0.2f))
    }
}

@Composable
fun TransportExpenseRow(id: Int, name: String, amount: Int,category: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(id.toString(), modifier = Modifier
            .weight(0.1f))
        Text(name, modifier = Modifier.weight(0.2f))
        Text(category, modifier = Modifier.weight(0.2f))
        Text(amount.toString(), modifier = Modifier.weight(0.2f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransportCustomTextField(
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
fun TransportMainContent(name: String, modifier: Modifier = Modifier) {
    Column() {
        TitleRow(head1 = "ID", head2 = "Name", head3 = "Location", head4 = "Amount")
        CustomTextField(title = "ID", textState = "1", onTextChange = {}, keyboardType = KeyboardType.Number)
    }
}

class TransportExpensesViewModelFactory(val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExpensesViewModel(application) as T
    }
}