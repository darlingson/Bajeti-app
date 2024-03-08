package com.codeshinobi.bajeti.newUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeSummary() {
//    Text(text = "Summary")
    Column(
        modifier = Modifier.fillMaxSize()
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
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Budget : K10,000")
            Text(text = "Current Spend : K10,000")
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