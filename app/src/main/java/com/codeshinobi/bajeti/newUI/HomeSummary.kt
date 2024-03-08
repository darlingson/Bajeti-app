package com.codeshinobi.bajeti.newUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeSummary() {
//    Text(text = "Summary")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SummaryBudgetCard()
        Divider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )
        MonthlyExpensesSummaryList()
    }
}

@Composable
fun SummaryBudgetCard() {
    var budget: Double = 100000.0
    var currentSpend: Double = 80000.0
    val spendPercentage = (currentSpend / budget) * 100
    val backgroundColor = when {
        spendPercentage < 70 -> Color.Green
        spendPercentage in 71.0..99.0 -> Color.Magenta
        else -> Color.Red
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Budget : K${budget.toInt()}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Current Spend : K${currentSpend.toInt()}",
                color = backgroundColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun MonthlyExpensesSummaryList() {
    Text(text = "Expenses Summary")
    Column {
        ExpenseCard(name = "Spaghetti", amount = "1,200", category = "Food", date = "2023-05-01")
        ExpenseCard(name = "Spaghetti", amount = "1,200", category = "Food", date = "2023-05-01")
        ExpenseCard(name = "Spaghetti", amount = "1,200", category = "Food", date = "2023-05-01")
        ExpenseCard(name = "Spaghetti", amount = "1,200", category = "Food", date = "2023-05-01")
        ExpenseCard(name = "Spaghetti", amount = "1,200", category = "Food", date = "2023-05-01")
        ExpenseCard(name = "Spaghetti", amount = "1,200", category = "Food", date = "2023-05-01")
        ExpenseCard(name = "Spaghetti", amount = "1,200", category = "Food", date = "2023-05-01")
    }
}

@Composable
fun ExpenseCard(
    name: String,
    amount: String,
    category: String,
    date: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // First Row: Display Amount
            Text(
                text = "$amount MWK",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Second Row: Display Date, Name, and Category
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    text = "$name - $category",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeSummaryPreview() {
    HomeSummary()
}