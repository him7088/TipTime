package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
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
    @StringRes label : Int ,
    @DrawableRes leadingIcon : Int ,
    keyboardOptions: KeyboardOptions,
    onValueChange : (String) -> Unit,
    modifier: Modifier = Modifier
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null)},
        label = { Text(text = stringResource(id = label))},
        keyboardOptions = keyboardOptions,
        modifier = modifier

    )
}

@Composable
fun TipTimeLayout() {
    var amountInput by remember { mutableStateOf("0") }
    var tipInput by remember { mutableStateOf("") }
    var roundUp by remember { mutableStateOf(false)}
    val amount =amountInput.toDoubleOrNull() ?: 0.00
    val tipPercent= tipInput.toDoubleOrNull() ?: 0.00
    val tip= calculateTip(amount,tipPercent,roundUp)
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
        Spacer(modifier = Modifier.height(150.dp))
        Text(
            text = stringResource(id = R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start),
            fontSize = 24.sp
        )
        EditNumberField(
            value = amountInput,
            onValueChange ={amountInput = it} ,
            label = R.string.bill_amount,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType =KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth()
        )
        EditNumberField(
            value = tipInput,
            onValueChange ={tipInput = it} ,
            label = R.string.how_was_the_service,
            leadingIcon = R.drawable.percent,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType =KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth()
        )
        RoundTheTipRow(
            roundUp =roundUp ,
            onRoundUpChanged = { roundUp = it },
            modifier = Modifier
        )
        Text(
            text = stringResource(R.string.tip_amount,tip),
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(5.dp)
        )

    }
}
@Composable
fun RoundTheTipRow(
    roundUp : Boolean,
    onRoundUpChanged : (Boolean) -> Unit ,
    modifier: Modifier) {
    Row (
        modifier= Modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = stringResource(R.string.round_up_tip))
        Switch(checked = roundUp, onCheckedChange = onRoundUpChanged )
    }
}
@VisibleForTesting
internal fun calculateTip(amount : Double , tipPercent : Double ,roundUp: Boolean) : String{
    var tip= tipPercent / 100 * amount
    if(roundUp)
        tip= Math.ceil(tip)
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