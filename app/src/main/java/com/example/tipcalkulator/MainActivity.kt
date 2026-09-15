package com.example.tipcalkulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.input.KeyboardType
import com.example.tipcalkulator.ui.theme.TipCalkulatorTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalkulatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TipCalcScreen()
                }
            }
        }
    }
}

// ---------- ФУНКЦИИ РАСЧЁТОВ ----------

fun calculateDiscountPercent(dishCount: Int): Int {
    return when {
        dishCount <= 0 -> 0
        dishCount in 1..2 -> 3
        dishCount in 3..5 -> 5
        dishCount in 6..10 -> 7
        else -> 10
    }
}

fun calculateDiscountAmount(orderSum: Double, dishCount: Int): Double {
    val percent = calculateDiscountPercent(dishCount)
    return orderSum * percent / 100.0
}

fun calculateTips(orderSum: Double, tipsPercent: Int): Double {
    return orderSum * tipsPercent / 100.0
}

fun calculateTotal(orderSum: Double, dishCount: Int, tipsPercent: Int): Double {
    val discount = calculateDiscountAmount(orderSum, dishCount)
    val tips = calculateTips(orderSum, tipsPercent)
    return orderSum - discount + tips
}

@Composable
fun TipCalcScreen() {

    var orderSumText by remember { mutableStateOf("") }
    var dishCountText by remember { mutableStateOf("") }
    var tipsPercent by remember { mutableStateOf(0f) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val orderSum = orderSumText.toDoubleOrNull() ?: 0.0
    val dishCount = dishCountText.toIntOrNull() ?: 0
    val discountPercent = calculateDiscountPercent(dishCount)
    var resultText by remember { mutableStateOf("") }
    var isTotalShown by remember { mutableStateOf(false) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Сумма заказа:", modifier = Modifier.width(150.dp))
                OutlinedTextField(
                    value = orderSumText,
                    onValueChange = {
                        orderSumText = it
                        isTotalShown = false
                    },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Количество блюд:", modifier = Modifier.width(150.dp))
                OutlinedTextField(
                    value = orderSumText,
                    onValueChange = {
                        orderSumText = it
                        isTotalShown = false
                    },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Text("Чаевые: ${tipsPercent.toInt()}%")
            Slider(
                value = tipsPercent,
                onValueChange = { tipsPercent = it },
                valueRange = 0f..25f,
                steps = 4,
                onValueChangeFinished = {
                    val tips = calculateTips(orderSum, tipsPercent.toInt())
                    scope.launch {
                        snackbarHostState.showSnackbar("Сумма чаевых: %.2f".format(tips))
                    }
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("0")
                Text("25")
            }

            Text("Скидка:")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(3, 5, 7, 10).forEach { percent ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        RadioButton(
                            selected = discountPercent == percent,
                            onClick = { /* выбор программный */ },
                            enabled = false
                        )
                        Text("$percent%")
                    }
                }
            }

            OutlinedTextField(
                value = resultText,
                onValueChange = {},
                readOnly = true,
                label = { Text(if (isTotalShown) "Итого" else "Сумма скидки") },
                modifier = Modifier.fillMaxWidth()
            )

            LaunchedEffect(orderSum, dishCount, isTotalShown) {
                if (!isTotalShown) {
                    val discount = calculateDiscountAmount(orderSum, dishCount)
                    resultText = "%.2f".format(discount)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TipCalcPreview() {
    TipCalkulatorTheme {
        TipCalcScreen()
    }
}

