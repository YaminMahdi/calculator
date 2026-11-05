package com.mlab.calculator

import com.mlab.calculator.compose.CalculatorViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculatorViewModelTest {

    private lateinit var viewModel: CalculatorViewModel

    @Before
    fun setUp() {
        viewModel = CalculatorViewModel()
    }

    @Test
    fun testInitialState() {
        val state = viewModel.uiState.value
        assertEquals("", state.expression)
        assertEquals("0", state.result)
    }

    @Test
    fun testNumberInput() {
        viewModel.onNumber("5")
        viewModel.onNumber("6")
        val state = viewModel.uiState.value
        assertEquals("56", state.expression)
        assertEquals("56", state.result)
    }

    @Test
    fun testBasicAddition() {
        viewModel.onNumber("1")
        viewModel.onNumber("2")
        viewModel.onOperator("+")
        viewModel.onNumber("8")
        viewModel.onEquals()
        val state = viewModel.uiState.value
        assertEquals("20", state.result)
    }

    @Test
    fun testOperatorPrecedence() {
        // 2 + 3 × 4 = 14 (not 20)
        viewModel.onNumber("2")
        viewModel.onOperator("+")
        viewModel.onNumber("3")
        viewModel.onOperator("×")
        viewModel.onNumber("4")
        viewModel.onEquals()
        val state = viewModel.uiState.value
        assertEquals("14", state.result)
    }

    @Test
    fun testBackspace() {
        viewModel.onNumber("1")
        viewModel.onNumber("2")
        viewModel.onNumber("3")
        viewModel.onBackspace()
        val state = viewModel.uiState.value
        assertEquals("12", state.expression)
    }

    @Test
    fun testClear() {
        viewModel.onNumber("9")
        viewModel.onOperator("×")
        viewModel.onNumber("9")
        viewModel.onClear()
        val state = viewModel.uiState.value
        assertEquals("", state.expression)
        assertEquals("0", state.result)
    }
}
