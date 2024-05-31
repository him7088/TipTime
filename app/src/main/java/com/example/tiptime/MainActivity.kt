package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipTimeTheme {
                Surface {
                    TipTimeLayout()
                }
            }
        }
    }
}
@Composable
fun EditNumberField(
    value : String,
    onValueChange : (String) -> Unit,
    modifier: Modifier = Modifier
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.bill_amount))},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier

    )
}

@Composable
fun TipTimeLayout() {
    var amountInput by remember { mutableStateOf("0") }
    val amount =amountInput.toDoubleOrNull() ?: 0.00
    val tip= calculateTip(amount,15.0)
    Column(
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding()
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(value = amountInput, onValueChange ={amountInput = it} , modifier = Modifier )

        Text(
            text = stringResource(R.string.tip_amount,tip),
            style = MaterialTheme.typography.displaySmall)
        Spacer(modifier = Modifier.height(150.dp))
    }
}

private fun calculateTip(amount : Double , tipPercent : Double) : String{
    val tip= tipPercent / 100 * amount
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true,
    showSystemUi = true)
@Composable
fun GreetingPreview() {
    TipTimeTheme {
        TipTimeLayout()
    }
}