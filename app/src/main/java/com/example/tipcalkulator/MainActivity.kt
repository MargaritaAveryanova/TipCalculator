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
import com.example.tipcalkulator.ui.theme.TipCalkulatorTheme

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

@Composable
fun TipCalcScreen() {
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
                    value = "",
                    onValueChange = {},
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Количество блюд:", modifier = Modifier.width(150.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
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

