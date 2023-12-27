package com.codeshinobi.bajeti

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.codeshinobi.bajeti.Models.ExpensesDatabase
import com.codeshinobi.bajeti.Repositories.ExpenseRepository
import com.codeshinobi.bajeti.Repositories.MonthTotalRepository
import com.codeshinobi.bajeti.Repositories.OtherExpensesRepository
import com.codeshinobi.bajeti.Repositories.TransportExpensesRepository
import com.codeshinobi.bajeti.Repositories.UtilityExpensesRepository
import com.codeshinobi.bajeti.activities.ExpensesActivity
import com.codeshinobi.bajeti.activities.MonthBudgetActivity
import com.codeshinobi.bajeti.activities.OtherExpensesActivity
import com.codeshinobi.bajeti.activities.TransportExpensesActivity
import com.codeshinobi.bajeti.activities.UtilitiesActivity
import com.codeshinobi.bajeti.activities.screens.Drawer
import com.codeshinobi.bajeti.ui.theme.BajetiTheme
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BajetiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val drawerState = rememberDrawerState(DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    val openDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                    val context = LocalContext.current
                    ModalNavigationDrawer(
                        drawerContent = {
                            Drawer(
                                modifier = Modifier.fillMaxHeight(),
                                onDestinationClicked = {
                                    openDrawer()
                                }
                            )
                        },
                        drawerState = drawerState,

                    ) {
                        
                    }
                    Column() {
                        WelcomeCard()
                        MainOptions()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
@Composable
fun MainOptions() {
    Column {
        MainOptionsCard(text = "Food", modifier = Modifier.fillMaxWidth(), ExpensesActivity::class.java)
        MainOptionsCard(text = "Transportation", modifier = Modifier.fillMaxWidth(),TransportExpensesActivity::class.java)
        MainOptionsCard(text = "Other Expenses", modifier = Modifier.fillMaxWidth(),OtherExpensesActivity::class.java)
        MainOptionsCard(text = "Utilities", modifier = Modifier.fillMaxWidth(),UtilitiesActivity::class.java)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainOptionsCard(text: String,
                    modifier: Modifier = Modifier.fillMaxWidth(),
                    activity: Class<*>
)
{

    val context = LocalContext.current //getting the context

    //getting the repository and DAO for the expenseEntity
    val expenseRepository: ExpenseRepository?
    val expensesDatabase = ExpensesDatabase.getInstance(context)
    val expenseDAO = expensesDatabase?.ExpenseDAO
    expenseRepository = expenseDAO?.let { ExpenseRepository(it) }

    //getting the sum of expenses and setting it into a state
    val sumOfFood: State<Int?>? = expenseRepository?.sumofExpenses?.observeAsState(initial = 0)



    //getting the repository and DAO for the expenseEntity
    val transportRepository: TransportExpensesRepository?
//    val expensesDatabase = ExpensesDatabase.getInstance(context)
    val transportexpenseDAO = expensesDatabase?.TransportExpenseDAO
    transportRepository = transportexpenseDAO?.let { TransportExpensesRepository(it) }

    //getting the sum of expenses and setting it into a state
    val sumOfTransport: State<Int?>? = transportRepository?.sumofTransportExpenses?.observeAsState(initial = 0)

    val otherExpensesRepository :OtherExpensesRepository?
    val otherExpensesDAO = expensesDatabase?.OtherExpensesDAO
    otherExpensesRepository = otherExpensesDAO?.let { OtherExpensesRepository(it) }
    val sumOfOtherExpenses: State<Int?>? = otherExpensesRepository?.sumofOtherExpenses?.observeAsState(initial = 0)

    val utilityExpensesRepository: UtilityExpensesRepository?
    val utilityExpensesDAO = expensesDatabase?.UtilitiesDAO
    utilityExpensesRepository = utilityExpensesDAO?.let { UtilityExpensesRepository(it) }
    val sumOfUtilityExpenses:State<Int?>? = utilityExpensesRepository?.sumofUtilityExpenses?.observeAsState(initial = 0)

    Card(
        shape =MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier
            .padding(10.dp)
            .height(80.dp),
        onClick = { Log.d("Click", "CardExample: Card Click")
//            context.startActivity(Intent(context, MainOption::class.java))
            context.startActivity(Intent(context, activity))
            },
        enabled = true
    ) {
        Row(horizontalArrangement = Arrangement.Absolute.SpaceBetween) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(0.49f)
            ) {
                Text(
                    text = text,
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.49f)
            ) {
                if (activity == ExpensesActivity::class.java){
                    sumOfFood?.value?.let {
                        Text(
                            text = sumOfFood.value.toString(),
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.End
                        )
                    }
                }else if (activity == TransportExpensesActivity::class.java){
                    sumOfTransport?.value?.let {
                        Text(
                            text = sumOfTransport.value.toString(),
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else if (activity == OtherExpensesActivity::class.java){
                    sumOfOtherExpenses?.value?.let {
                        Text(
                            text = sumOfOtherExpenses.value.toString(),
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else if (activity == UtilitiesActivity::class.java){
                    sumOfUtilityExpenses?.value?.let {
                        Text(
                            text = sumOfUtilityExpenses.value.toString(),
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeCard(){
    val dateFormat: DateFormat = SimpleDateFormat("MM")
    val date = Date()
    var currentMonth = dateFormat.format(date)

    val cal: Calendar = Calendar.getInstance()
    val year: Int = cal.get(Calendar.YEAR)
    val mouth: Int = cal.get(Calendar.MONTH)
    val day: Int = cal.get(Calendar.DATE)
    val currentIndex = (year - 1970) * 12 + mouth

    //retrieving the current month budget
    val context = LocalContext.current
    val monthTotalRepository: MonthTotalRepository?
    val expensesDatabase = ExpensesDatabase.getInstance(context)
    val monthTotalDAO = expensesDatabase?.MonthlyTotalDAO
    monthTotalRepository = monthTotalDAO?.let { MonthTotalRepository(it) }

    val sumOfBudget: State<Int>? = monthTotalRepository?.gettotalBudget(currentIndex)?.observeAsState(initial = 0)

    Card(
        shape =MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        onClick = {
            Log.d("Click", "CardExample: Card Click")
            context.startActivity(Intent(context,MonthBudgetActivity::class.java))
//            context.startActivity(Intent(context, ExpensesActivity::class.java))
                  },
        enabled = true
    ) {
        Column() {
            Greeting("Darlingson")
            Text(text = "Welcome")
            sumOfBudget?.value?.let {
                Text(
                    text = sumOfBudget.value.toString(),
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BajetiTheme {
//        Greeting("Darlingson")
        MainOptions()
    }
}