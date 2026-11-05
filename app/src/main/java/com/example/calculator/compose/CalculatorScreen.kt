package com.example.calculator.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = remember { CalculatorViewModel() },
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // 1. Display Area (Expression & Large Result)
        CalculatorDisplay(
            expression = uiState.expression,
            result = uiState.result,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
        )

        // 2. Toolbar Icons (History, Mode, Settings, Backspace)
        CalculatorToolbar(
            onHistoryClick = { /* Handle History */ },
            onModeClick = { /* Handle Scientific Mode */ },
            onSettingsClick = { /* Handle Settings */ },
            onBackspaceClick = { viewModel.onBackspace() },
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 3. Keypad Matrix (5 Rows matching demo69.png)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3.5f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Row 1: C, MC, %, ÷
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CalculatorButton("C", ButtonOperatorBg, ButtonOperatorText, { viewModel.onClear() }, Modifier.weight(1f))
                CalculatorButton("MC", ButtonOperatorBg, ButtonOperatorText, { viewModel.onMemoryClear() }, Modifier.weight(1f), fontSize = 20)
                CalculatorButton("%", ButtonOperatorBg, ButtonOperatorText, { viewModel.onOperator("%") }, Modifier.weight(1f))
                CalculatorButton("÷", ButtonOperatorBg, ButtonOperatorText, { viewModel.onOperator("÷") }, Modifier.weight(1f), fontSize = 28)
            }

            // Row 2: 7, 8, 9, ×
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CalculatorButton("7", ButtonNumberBg, ButtonNumberText, { viewModel.onNumber("7") }, Modifier.weight(1f))
                CalculatorButton("8", ButtonNumberBg, ButtonNumberText, { viewModel.onNumber("8") }, Modifier.weight(1f))
                CalculatorButton("9", ButtonNumberBg, ButtonNumberText, { viewModel.onNumber("9") }, Modifier.weight(1f))
                CalculatorButton("×", ButtonOperatorBg, ButtonOperatorText, { viewModel.onOperator("×") }, Modifier.weight(1f), fontSize = 26)
            }

            // Row 3: 4, 5, 6, -
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CalculatorButton("4", ButtonNumberBg, ButtonNumberText, { viewModel.onNumber("4") }, Modifier.weight(1f))
                CalculatorButton("5", ButtonNumberBg, ButtonNumberText, { viewModel.onNumber("5") }, Modifier.weight(1f))
                CalculatorButton("6", ButtonNumberBg, ButtonNumberText, { viewModel.onNumber("6") }, Modifier.weight(1f))
                CalculatorButton("-", ButtonOperatorBg, ButtonOperatorText, { viewModel.onOperator("-") }, Modifier.weight(1f), fontSize = 28)
            }

            // Row 4: 1, 2, 3, +
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CalculatorButton("1", ButtonNumberBg, ButtonNumberText, { viewModel.onNumber("1") }, Modifier.weight(1f))
                CalculatorButton("2", ButtonNumberBg, ButtonNumberText, { viewModel.onNumber("2") }, Modifier.weight(1f))
                CalculatorButton("3", ButtonNumberBg, ButtonNumberText, { viewModel.onNumber("3") }, Modifier.weight(1f))
                CalculatorButton("+", ButtonOperatorBg, ButtonOperatorText, { viewModel.onOperator("+") }, Modifier.weight(1f), fontSize = 28)
            }

            // Row 5: 0 (Double Width), •, =
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Wide 0 capsule spanning 2 column weights!
                CalculatorButton("0", ButtonNumberBg, ButtonNumberText, { viewModel.onNumber("0") }, Modifier.weight(2f))
                CalculatorButton("•", ButtonNumberBg, ButtonNumberText, { viewModel.onNumber(".") }, Modifier.weight(1f))
                CalculatorButton("=", ButtonEqualsBg, ButtonEqualsText, { viewModel.onEquals() }, Modifier.weight(1f), fontSize = 32)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D232C)
@Composable
fun CalculatorScreenPreview() {
    CalculatorComposeTheme {
        CalculatorScreen()
    }
}
