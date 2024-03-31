package com.codeshinobi.bajeti.newUI

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import com.codeshinobi.bajeti.newUI.ViewModels.BudgetViewModel

@Composable
fun ReportsScreen(viewModel: BudgetViewModel) {
    Column(Modifier.fillMaxSize()) {
        Text(text = "Reports")
        BarChartComposable()
    }
}
@Composable
fun BarChartComposable() {
    var trialBarData = BarData(label = "January", point = Point(x=0f, y=10f))
    val barChartListSize = 10
    val maxRange = 100
    val yStepSize = 10
    val barData = DataUtils.getBarChartData(barChartListSize, maxRange)
    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(barData.size - 1)
        .bottomPadding(40.dp)
        .axisLabelAngle(20f)
        .labelData { index -> barData[index].label }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(yStepSize)
        .labelAndAxisLinePadding(20.dp)
        .axisOffset(20.dp)
        .labelData { index -> (index * (maxRange / yStepSize)).toString() }
        .build()
    val barChartDataClass = BarChartData(
        chartData = barData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
    )
    BarChart(modifier = Modifier.height(350.dp), barChartData = barChartDataClass)
}