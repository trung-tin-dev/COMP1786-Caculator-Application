package com.example.caculatorapp;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
//    Declare Variables
    private TextView txtDisplay;
    private TextView opDisplay;
    private double firstNumber = 0;
    private double secondNumber = 0;
    private String operator = "";
    private boolean hasJustCalculated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//      Bind Views
        txtDisplay = findViewById(R.id.txtDisplay);
        opDisplay = findViewById(R.id.opDisplay);

        Button btn0 = findViewById(R.id.btn0);
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        Button btn6 = findViewById(R.id.btn6);
        Button btn7 = findViewById(R.id.btn7);
        Button btn8 = findViewById(R.id.btn8);
        Button btn9 = findViewById(R.id.btn9);

        Button btnPlus = findViewById(R.id.btnPlus);
        Button btnMinus = findViewById(R.id.btnMinus);
        Button btnMultiply = findViewById(R.id.btnMultiply);
        Button btnDivide = findViewById(R.id.btnDivide);

        Button btnEqual = findViewById(R.id.btnEqual);
        Button btnClear = findViewById(R.id.btnClear);

        btn0.setOnClickListener(v -> appendNumber("0"));
        btn1.setOnClickListener(v -> appendNumber("1"));
        btn2.setOnClickListener(v -> appendNumber("2"));
        btn3.setOnClickListener(v -> appendNumber("3"));
        btn4.setOnClickListener(v -> appendNumber("4"));
        btn5.setOnClickListener(v -> appendNumber("5"));
        btn6.setOnClickListener(v -> appendNumber("6"));
        btn7.setOnClickListener(v -> appendNumber("7"));
        btn8.setOnClickListener(v -> appendNumber("8"));
        btn9.setOnClickListener(v -> appendNumber("9"));

        btnPlus.setOnClickListener(v -> setOperator("+"));
        btnMinus.setOnClickListener(v -> setOperator("-"));
        btnMultiply.setOnClickListener(v -> setOperator("x"));
        btnDivide.setOnClickListener(v -> setOperator("/"));
        btnEqual.setOnClickListener(v -> calculate());
        btnClear.setOnClickListener(v -> {
            firstNumber = 0;
            secondNumber = 0;
            operator = "";
            txtDisplay.setText(getString(R.string.lbl_0));
            hasJustCalculated = false;
            opDisplay.setText("");
            txtDisplay.setTextColor(ContextCompat.getColor(this, R.color.display_text));
        });
    }

//    Handle Number Input
    private void appendNumber(String number) {
        String currentText = txtDisplay.getText().toString();

        if (currentText.equals(getString(R.string.error_message)) || hasJustCalculated) {
            txtDisplay.setText(number);
            txtDisplay.setTextColor(Color.BLACK);
            hasJustCalculated = false;
            return;
        }

        if (currentText.equals(getString(R.string.lbl_0))) {
            txtDisplay.setText(number);
        } else {
            txtDisplay.append(number);
        }
    }

//    Handle Operator input and Chained Math
    private void setOperator(String op) {
        String currentText = txtDisplay.getText().toString();

        if (currentText.equals(getString(R.string.error_message))) return;

        if (!hasJustCalculated) {
            if (operator.isEmpty()) {
                firstNumber = Double.parseDouble(currentText);
            } else {
                int opIndex = currentText.indexOf(operator);
                if (opIndex != -1) {
                    try {
                        String secondPart = currentText.substring(opIndex + operator.length()).trim();
                        if (!secondPart.isEmpty()) {
                            secondNumber = Double.parseDouble(secondPart);
                            firstNumber = MathUtil.execute(firstNumber, secondNumber, operator);
                        }
                    } catch (Exception e) {
                        android.util.Log.e("CalculatorApp", "Error parsing intermediate second part", e);
                    }
                }
            }
        }

        operator = op;
        hasJustCalculated = false;

        String formattedFirst = (firstNumber == (int) firstNumber) ? String.valueOf((int) firstNumber) : String.valueOf(firstNumber);

        txtDisplay.setText(getString(R.string.expression_format, formattedFirst, operator));
        opDisplay.setText(operator);
    }

//    Core Calculation
    private void calculate() {
        if (operator.isEmpty()) {
            return;
        }

        String currentText = txtDisplay.getText().toString();

        if (!hasJustCalculated) {
            int opIndex = currentText.indexOf(operator);
            if (opIndex != -1) {
                try {
                    String secondPart = currentText.substring(opIndex + operator.length()).trim();
                    if (!secondPart.isEmpty()) {
                        secondNumber = Double.parseDouble(secondPart);
                    } else {
                        secondNumber = firstNumber;
                    }
                } catch (Exception e) {
                    return;
                }
            }
        }

        double result = 0;
        switch (operator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "x":
                result = firstNumber * secondNumber;
                break;
            case "/":
                if (secondNumber == 0) {
                    txtDisplay.setTextColor(ContextCompat.getColor(this, R.color.error_red));
                    txtDisplay.setText(getString(R.string.error_message));
                    return;
                }
                result = firstNumber / secondNumber;
                break;
        }

        opDisplay.setText(operator);

        if (result == (int) result) {
            txtDisplay.setText(String.valueOf((int) result));
        } else {
            txtDisplay.setText(String.valueOf(result));
        }

        firstNumber = result;
        hasJustCalculated = true;
    }

    private static class MathUtil {
        static double execute(double a, double b, String op) {
            switch (op) {
                case "+": return a + b;
                case "-": return a - b;
                case "x": return a * b;
                case "/": return b != 0 ? a / b : 0;
                default: return 0;
            }
        }
    }
}