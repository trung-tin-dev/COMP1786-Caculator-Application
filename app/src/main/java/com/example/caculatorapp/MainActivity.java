package com.example.caculatorapp;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView txtDisplay;
    private TextView opDisplay;

    private double firstNumber = 0;
    private String operator = "";

    private boolean isNewNumber = true;

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
            operator = "";
            txtDisplay.setText("0");
            isNewNumber = true;
            opDisplay.setText("");
            txtDisplay.setTextColor(getResources().getColor(R.color.display_text));
        });
    }

    private void appendNumber(String number) {
        String currentText = txtDisplay.getText().toString();
        if (currentText.equals("Error")) {
            txtDisplay.setText(number);
            txtDisplay.setTextColor(Color.BLACK);
            isNewNumber = false;
            return;
        }
        if (isNewNumber) {
            txtDisplay.setText(number);
            isNewNumber = false;
        } else {
            txtDisplay.append(number);
        }
    }

    private void setOperator(String op) {
        firstNumber = Double.parseDouble(txtDisplay.getText().toString());
        operator = op;
        opDisplay.setText(op);
        isNewNumber = true;
    }

    private void calculate() {
        if (operator.isEmpty()) {
            return;
        }
        double secondNumber =
                Double.parseDouble(
                        txtDisplay.getText().toString()
                );
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
                    txtDisplay.setTextColor(getResources().getColor(R.color.error_red));
                    txtDisplay.setText("Error");
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
        isNewNumber = true;
    }
}