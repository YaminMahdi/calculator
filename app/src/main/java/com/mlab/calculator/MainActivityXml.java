package com.mlab.calculator;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.Stack;

public class MainActivityXml extends Activity implements View.OnClickListener {

    private TextView tvExpression;
    private TextView tvResult;
    private StringBuilder currentExpr = new StringBuilder();
    private DecimalFormat df = new DecimalFormat("#,###.########");
    private double memoryValue = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Set ContentView first so DecorView is initialized
        setContentView(R.layout.activity_main);

        // 2. Enable Edge-to-Edge display safely
        enableEdgeToEdge();

        // 3. Apply Window Insets for status bar and gesture navigation bar
        applyWindowInsets();

        tvExpression = findViewById(R.id.tv_expression);
        tvResult = findViewById(R.id.tv_result);

        // Keypad buttons
        int[] buttonIds = {
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
            R.id.btn_dot, R.id.btn_plus, R.id.btn_minus, R.id.btn_multiply,
            R.id.btn_divide, R.id.btn_percent, R.id.btn_clear, R.id.btn_mc,
            R.id.btn_equals
        };

        for (int id : buttonIds) {
            Button b = findViewById(id);
            if (b != null) {
                b.setOnClickListener(this);
            }
        }

        // Top toolbar buttons
        int[] toolIds = {
            R.id.btn_history, R.id.btn_mode, R.id.btn_settings, R.id.btn_backspace
        };

        for (int id : toolIds) {
            ImageButton ib = findViewById(id);
            if (ib != null) {
                ib.setOnClickListener(this);
            }
        }
    }

    private void enableEdgeToEdge() {
        Window window = getWindow();
        if (window == null) return;

        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false);
            try {
                WindowInsetsController controller = window.getInsetsController();
                if (controller != null) {
                    controller.setSystemBarsAppearance(0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS |
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
                }
            } catch (Exception ignored) {
            }
        } else {
            View decor = window.getDecorView();
            if (decor != null) {
                decor.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                );
            }
        }
    }

    private void applyWindowInsets() {
        View mainLayout = findViewById(R.id.main_layout);
        if (mainLayout != null) {
            mainLayout.setOnApplyWindowInsetsListener((v, insets) -> {
                int padLeft = dpToPx(20);
                int padRight = dpToPx(20);
                int padTop = dpToPx(8);
                int padBottom = dpToPx(20);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Insets systemInsets = insets.getInsets(
                        WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout()
                    );
                    v.setPadding(
                        padLeft + systemInsets.left,
                        padTop + systemInsets.top,
                        padRight + systemInsets.right,
                        padBottom + systemInsets.bottom
                    );
                } else {
                    v.setPadding(
                        padLeft + insets.getSystemWindowInsetLeft(),
                        padTop + insets.getSystemWindowInsetTop(),
                        padRight + insets.getSystemWindowInsetRight(),
                        padBottom + insets.getSystemWindowInsetBottom()
                    );
                }
                return insets;
            });
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_clear) {
            currentExpr.setLength(0);
            tvExpression.setText("");
            tvResult.setText("0");
        } else if (id == R.id.btn_mc) {
            memoryValue = 0.0;
            Toast.makeText(this, "Memory Cleared", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_backspace) {
            performBackspace();
        } else if (id == R.id.btn_equals) {
            calculateFinal();
        } else if (id == R.id.btn_history) {
            Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_mode) {
            Toast.makeText(this, "Scientific Mode", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_settings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        } else {
            Button b = (Button) v;
            String text = b.getText().toString();
            if (text.equals("•")) {
                text = ".";
            }
            currentExpr.append(text);
            tvExpression.setText(currentExpr.toString());
            updateLivePreview();
        }
    }

    private void performBackspace() {
        if (currentExpr.length() > 0) {
            currentExpr.deleteCharAt(currentExpr.length() - 1);
            tvExpression.setText(currentExpr.toString());
            updateLivePreview();
        }
    }

    private void updateLivePreview() {
        if (currentExpr.length() == 0) {
            tvResult.setText("0");
            return;
        }
        try {
            double res = evaluate(currentExpr.toString());
            tvResult.setText(df.format(res));
        } catch (Exception ignored) {
        }
    }

    private void calculateFinal() {
        if (currentExpr.length() == 0) return;
        try {
            double res = evaluate(currentExpr.toString());
            String formatted = df.format(res);
            tvResult.setText(formatted);
            currentExpr.setLength(0);
            currentExpr.append(formatted.replace(",", ""));
        } catch (Exception e) {
            tvResult.setText("Error");
        }
    }

    private double evaluate(String expression) throws Exception {
        String sanitized = expression.replaceAll(" ", "")
                                     .replaceAll("×", "*")
                                     .replaceAll("÷", "/");
        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < sanitized.length(); i++) {
            char c = sanitized.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < sanitized.length() && (Character.isDigit(sanitized.charAt(i)) || sanitized.charAt(i) == '.')) {
                    sb.append(sanitized.charAt(i++));
                }
                i--;
                values.push(Double.parseDouble(sb.toString()));
            } else if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                while (!ops.isEmpty() && ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                if (!ops.isEmpty()) ops.pop();
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%') {
                if (c == '-' && (i == 0 || sanitized.charAt(i - 1) == '(' || isOperator(sanitized.charAt(i - 1)))) {
                    // Unary minus
                    values.push(0.0);
                }
                while (!ops.isEmpty() && hasPrecedence(c, ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(c);
            }
        }

        while (!ops.isEmpty()) {
            if (ops.peek() == '(') {
                ops.pop();
                continue;
            }
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.isEmpty() ? 0 : values.pop();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if ((op1 == '*' || op1 == '/' || op1 == '%') && (op2 == '+' || op2 == '-')) return false;
        return true;
    }

    private double applyOp(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Divide by zero");
                return a / b;
            case '%': return a % b;
        }
        return 0;
    }
}
