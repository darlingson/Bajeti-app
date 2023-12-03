package com.codeshinobi.bajeti.activities

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.magnifier
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codeshinobi.bajeti.Models.ExpenseEntity
import com.codeshinobi.bajeti.Models.MonthBudget
import com.codeshinobi.bajeti.ViewModels.MonthBudgetViewModel
import com.codeshinobi.bajeti.activities.ui.theme.BajetiTheme

class MonthBudgetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BajetiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting4("Android")
                    val owner = LocalViewModelStoreOwner.current
                    owner?.let {
                        val viewModel: MonthBudgetViewModel = viewModel(
                            it,
                            "MonthBudgetViewModel",
                            MonthBudgetViewModelFactory(
                                LocalContext.current.applicationContext as Application
                            )
                        )
                        MonthBudgetScreenSetup(viewModel)
                    }
                }
            }
        }
    }
}
@Composable
fun MonthBudgetScreenSetup(viewModel: MonthBudgetViewModel) {
    val allBudgets:List<MonthBudget> by viewModel.allBudgets.observeAsState(listOf())

    MonthBudgetMainScreen(allBudgets, viewModel)
}
@Composable
fun MonthBudgetMainScreen(allBudgets:List<MonthBudget>, viewModel: MonthBudgetViewModel) {
    var expenseName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var expenseType by remember { mutableStateOf("") }
    var monthNumber by remember { mutableStateOf("") }

    var searching by remember { mutableStateOf(false) }

    val onExpenseNameTextChange = { text: String ->
        expenseName = text
    }
    val onAmountTextChange = { text: String ->
        amount = text
    }
    val onExpenseTypeTextChange = { text: String ->
        expenseType = text
    }
    val onMonthNumberTextChange = { text: String ->
        monthNumber = text
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        CustomTextField(
            title = "Expense Name",
            textState = expenseName,
            onTextChange = onExpenseNameTextChange,
            keyboardType = KeyboardType.Text
        )
        CustomTextField(
            title = "Amount",
            textState = amount,
            onTextChange = onAmountTextChange,
            keyboardType = KeyboardType.Number
        )
        CustomTextField(
            title = "Expense Type",
            textState = expenseType,
            onTextChange = onExpenseTypeTextChange,
            keyboardType = KeyboardType.Text
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ){
            Button(onClick = {
                if (amount.isNotEmpty()) {
                    viewModel.insertMonthBudget(
                        MonthBudget(
                            expenseName = expenseName,
                            expenseType = expenseType,
                            monthNumber = monthNumber.toInt(),
                            budgetAmount = amount.toInt()
                        )
                    )
                    searching = false
                }
            }) {
                Text("Add")
            }
        }

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)){
            item{
                BudgetTitleRow(
                    head1="Expense Name",
                    head2="Expense Type",
                    head3="Amount",
                    head4="Month Number"
                )
            }
            items(allBudgets.size){budget->
                allBudgets[budget].expenseName?.let {name->
                    allBudgets[budget].expenseType?.let { type ->
                        allBudgets[budget].budgetAmount?.let { amount ->
                            allBudgets[budget].monthNumber?.let { month ->
                                BudgetRow(
                                    name = name,
                                    type = type,
                                    amount = amount,
                                    monthNumber = month
                                )
                            }
                        }
                    }
                }

            }
        }
    }

}
@Composable
fun BudgetTitleRow(head1: String, head2: String,  head3: String, head4: String){
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
fun BudgetRow(
    name: String,
    type: String,
    amount: Int,
    monthNumber: Int
){
    Row(modifier =Modifier
        .fillMaxWidth()
        .padding(5.dp)
    ) {
        Text(name, color = Color.White,
            modifier = Modifier.weight(0.2f))
        Text(type, color = Color.White,
            modifier = Modifier.weight(0.2f))
        Text(amount.toString(), color = Color.White,
            modifier = Modifier.weight(0.2f))
        Text(monthNumber.toString(), color = Color.White,
            modifier = Modifier.weight(0.2f))
    }
}
@Composable
fun Greeting4(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview6() {
    BajetiTheme {
        Greeting4("Android")
    }
}

class MonthBudgetViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MonthBudgetViewModel(application) as T
    }
}