package com.example.simplecomposecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simplecomposecalculator.ui.theme.SimpleComposeCalculatorTheme
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.ui.text.input.ImeAction

//colors
import com.example.simplecomposecalculator.ui.theme.TipGreen
import com.example.simplecomposecalculator.ui.theme.TipBlue
import com.example.simplecomposecalculator.ui.theme.TipOrange
import com.example.simplecomposecalculator.ui.theme.LightGray

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleComposeCalculatorTheme {
                TipCalculatorApp()
            }
        }
    }
}

@Composable
fun TipCalculatorApp() {
    var tip by remember { mutableStateOf(0.0) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LightGray)
        ) {
            TipCalculator(
                amount = 100.0,
                modifier = Modifier.fillMaxSize(),
                onClick = { percentage ->
                    tip = 100 * percentage / 100.0
                }
            )
        }
    }
}

// Design inspiration: https://dribbble.com/shots/4292605-Daily-UI-004-Calculator
// minus the slider (looked a bit ugly so got rid of it)
@Composable
fun TipCalculator( amount: Double, modifier: Modifier = Modifier , onClick: (Int) -> Unit) {
    var selectedTip by remember { mutableStateOf<Int?>(15) }
    var customTip by remember { mutableStateOf("") }

    val tipPercentage = selectedTip ?: customTip.toIntOrNull() ?: 0
    val tip = amount * (tipPercentage / 100f)
    val total = amount + tip
    val currencySymbol = NumberFormat.getCurrencyInstance(Locale.getDefault()).currency?.symbol ?: ""

    val keyboardController = LocalSoftwareKeyboardController.current

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Results
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
                .background(TipGreen),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text( text = stringResource(R.string.total),  color = Color.White )
                Text(
                    text = "$currencySymbol ${String.format(Locale.getDefault(), "%.2f", total)}",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }

        
        // Amount and Tip Card
        Card (
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ){
            // Amount
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.amount))
                Text("$currencySymbol $amount")
            }

            // Tip amount
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.tip))
                Text( text = "$currencySymbol ${String.format(Locale.getDefault(), "%.2f", tip)}",)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp) // rounded top corners
                )
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ),

            contentAlignment = Alignment.TopCenter
        ){
            // Tip Percentage
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ){
                Text(stringResource(R.string.tip_percentage))

                // 3 buttons 10%, 15%, 20%
                Column {
                    // 10% button
                    TipOptionButton(
                        label = stringResource(R.string.tip_10),
                        isSelected = selectedTip == 10,
                        onClick = {
                            selectedTip = 10
                            customTip = ""
                            onClick(10)
                        }
                    )

                    // 15% button
                    TipOptionButton(
                        label = stringResource(R.string.tip_15),
                        isSelected = selectedTip == 15,
                        onClick = {
                            selectedTip = 15
                            customTip = ""
                            onClick(15)
                        }
                    )

                    // 20% button
                    TipOptionButton(
                        label = stringResource(R.string.tip_20),
                        isSelected = selectedTip == 20,
                        onClick = {
                            selectedTip = 20
                            customTip = ""
                            onClick(20)
                        }
                    )

                    FilledTonalButton(
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        onClick = { selectedTip = null },
                        colors = ButtonDefaults.buttonColors(containerColor = TipOrange)
                    ){
                        Text(stringResource(R.string.custom), color = Color.White )
                    }
                }

                if (selectedTip == null) {
                    OutlinedTextField(
                        value = customTip,
                        onValueChange = { newValue ->
                            customTip = newValue.filter { it.isDigit() }
                            onClick(customTip.toIntOrNull() ?: 0)
                        },
                        label = { Text(stringResource(R.string.enter_percentage)) },
                        keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Number, imeAction = ImeAction.Done ),
                        keyboardActions = KeyboardActions( onDone = { keyboardController?.hide() } ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TipOptionButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    if (isSelected) {
        Button(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = TipBlue)
        ) { Text(label, color = Color.White) }
    } else {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            onClick = onClick
        ) { Text(label, color = Color.Black) }
    }
}

@Preview(showBackground = true)
@Composable
fun TipPreview(){
    SimpleComposeCalculatorTheme {
        TipCalculator(amount = 100.0, modifier = Modifier, onClick = {})
    }
}