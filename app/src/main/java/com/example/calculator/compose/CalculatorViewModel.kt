package com.example.calculator.compose

import java.text.DecimalFormat
import java.util.Stack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CalculatorUiState(
    val expression: String = "",
    val result: String = "0"
)

class CalculatorViewModel {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    private val df = DecimalFormat("#,###.########")

    fun onNumber(digit: String) {
        val newExpr = _uiState.value.expression + digit
        _uiState.value = _uiState.value.copy(
            expression = newExpr,
            result = calculatePreview(newExpr)
        )
    }

    fun onOperator(op: String) {
        val current = _uiState.value.expression
        if (current.isNotEmpty()) {
            val lastChar = current.last()
            val newExpr = if (isOpChar(lastChar)) {
                current.dropLast(1) + op
            } else {
                current + op
            }
            _uiState.value = _uiState.value.copy(
                expression = newExpr,
                result = calculatePreview(newExpr)
            )
        }
    }

    fun onClear() {
        _uiState.value = CalculatorUiState(expression = "", result = "0")
    }

    fun onMemoryClear() {
        // Memory clear logic
    }

    fun onBackspace() {
        val current = _uiState.value.expression
        if (current.isNotEmpty()) {
            val newExpr = current.dropLast(1)
            _uiState.value = _uiState.value.copy(
                expression = newExpr,
                result = if (newExpr.isEmpty()) "0" else calculatePreview(newExpr)
            )
        }
    }

    fun onEquals() {
        val current = _uiState.value.expression
        if (current.isEmpty()) return
        try {
            val evaluated = evaluate(current)
            val formatted = df.format(evaluated)
            _uiState.value = _uiState.value.copy(
                expression = formatted,
                result = formatted
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(result = "Error")
        }
    }

    private fun isOpChar(c: Char) = c in listOf('+', '-', '×', '÷', '%')

    private fun calculatePreview(expr: String): String {
        return try {
            val res = evaluate(expr)
            df.format(res)
        } catch (e: Exception) {
            _uiState.value.result
        }
    }

    private fun evaluate(expression: String): Double {
        val sanitized = expression.replace(" ", "")
            .replace("×", "*")
            .replace("÷", "/")
        val values = Stack<Double>()
        val ops = Stack<Char>()

        var i = 0
        while (i < sanitized.length) {
            val c = sanitized[i]
            if (c.isDigit() || c == '.') {
                val sb = StringBuilder()
                while (i < sanitized.length && (sanitized[i].isDigit() || sanitized[i] == '.')) {
                    sb.append(sanitized[i++])
                }
                i--
                values.push(sb.toString().toDouble())
            } else if (c in listOf('+', '-', '*', '/', '%')) {
                if (c == '-' && (i == 0 || sanitized[i - 1] in listOf('+', '-', '*', '/', '%'))) {
                    values.push(0.0)
                }
                while (ops.isNotEmpty() && hasPrecedence(c, ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()))
                }
                ops.push(c)
            }
            i++
        }

        while (ops.isNotEmpty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()))
        }

        return if (values.isEmpty()) 0.0 else values.pop()
    }

    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        if ((op1 == '*' || op1 == '/' || op1 == '%') && (op2 == '+' || op2 == '-')) return false
        return true
    }

    private fun applyOp(op: Char, b: Double, a: Double): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> if (b == 0.0) throw ArithmeticException("Divide by zero") else a / b
            '%' -> a % b
            else -> 0.0
        }
    }
}
