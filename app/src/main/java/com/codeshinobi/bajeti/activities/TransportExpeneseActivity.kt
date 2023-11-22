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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codeshinobi.bajeti.Models.ExpenseEntity
import com.codeshinobi.bajeti.Models.TransportExpensesEntity
import com.codeshinobi.bajeti.ViewModels.ExpensesViewModel
import com.codeshinobi.bajeti.ViewModels.TransportExpensesViewModel
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
                        val viewModel: TransportExpensesViewModel = viewModel(
                            it,
                            "ExpensesViewModel",
                            TransportExpensesViewModelFactory(
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
fun TransportScreenSetup(viewModel: TransportExpensesViewModel) {

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
    allExpenses: List<TransportExpensesEntity>,
    searchResults: List<TransportExpensesEntity>,
    viewModel: TransportExpensesViewModel
) {
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var searching by remember { mutableStateOf(false) }

    val onLocationTextChange = { text : String ->
        location = text
    }
    val onAmountTextChange = { text : String ->
        amount = text
    }
    val onDateTextChange = { text : String ->
        date = text
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TransportCustomTextField(
            title = "Location",
            textState = location,
            onTextChange = onLocationTextChange,
            keyboardType = KeyboardType.Text
        )
        TransportCustomTextField(
            title = "Date",
            textState = date,
            onTextChange = onDateTextChange,
            keyboardType = KeyboardType.Text
        )

        TransportCustomTextField(
            title = "Expense Amount",
            textState = amount,
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
                if (amount.isNotEmpty()) {
                    viewModel.insertTransportExpense(
                        TransportExpensesEntity(
                            location = location,
                            date = date,
                            amount = amount.toInt()
                        )
                    )
                    searching = false
                }
            }) {
                Text("Add")
            }

            Button(onClick = {
                searching = true
                viewModel.findTransportExpense(location)
            }) {
                Text("Search")
            }

            Button(onClick = {
                searching = false
                viewModel.deleteTransportExpense(location)
            }) {
                Text("Delete")
            }

            Button(onClick = {
                searching = false
                location = ""
                amount = ""
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
                TransportTitleRow(head1 = "ID", head2 = "location", head3 = "Date",head4="Amount")
            }

            items(allExpenses.size) { expense ->
                allExpenses[expense].location?.let {
                    allExpenses[expense].amount?.let { it1 ->
                        allExpenses[expense].date?.let { it2 ->
                            TransportExpenseRow(id = allExpenses[expense].id, location = it,
                                date = it2, amount = it1
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
fun TransportExpenseRow(id: Int, location: String, amount: Int,date: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(id.toString(), modifier = Modifier
            .weight(0.1f))
        Text(location, modifier = Modifier.weight(0.2f))
        Text(date, modifier = Modifier.weight(0.2f))
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
        return TransportExpensesViewModel(application) as T
    }
}