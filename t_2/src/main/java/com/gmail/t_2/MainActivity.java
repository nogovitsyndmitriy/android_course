package com.gmail.t_2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gmail.t_2.service.MathService;
import com.gmail.t_2.service.impl.MathServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.t_2.SecondActivity.RESULT_1;
import static com.gmail.t_2.SecondActivity.RESULT_2;
import static com.gmail.t_2.SecondActivity.RESULT_3;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {


    public static final String VALUE_LIST = "valueList";
    private MathService mathService = new MathServiceImpl();

    private List<Integer> valueList;
    private TextView avgText;
    private TextView sumText;
    private TextView quotientText;
    private TextView text_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_1 = findViewById(R.id.text_1);

        avgText = findViewById(R.id.result_1);
        sumText = findViewById(R.id.result_2);
        quotientText = findViewById(R.id.result_3);
        avgText.setText(R.string.average);
        sumText.setText(R.string.sum);
        quotientText.setText(R.string.quotient);

        valueList = mathService.generateNewList();
        final StringBuilder values = getStringValues();
        text_1.setText("Generated numbers :  " + values.toString());

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);

                intent.putIntegerArrayListExtra(VALUE_LIST, (ArrayList<Integer>) valueList);
                startActivityForResult(intent, 1);
            }
        });

        findViewById(R.id.btnRestart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                startActivityForResult(intent, 666);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Double average = data.getDoubleExtra(RESULT_1, 0.0);
            Double sum = data.getDoubleExtra(RESULT_2, 0.0);
            Double quotient = data.getDoubleExtra(RESULT_3, 0.0);

            avgText.setText("Average of numbers:  " + average);
            sumText.setText("Sum of numbers:  " + sum);
            quotientText.setText("Quotient of numbers:  " + quotient);
        }
        if (requestCode == 666 && resultCode == Activity.RESULT_OK) {
            valueList = mathService.generateNewList();
            avgText.setText(R.string.average);
            sumText.setText(R.string.sum);
            quotientText.setText(R.string.quotient);

            final StringBuilder values = getStringValues();
            text_1.setText("Generated numbers :  " + values.toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private StringBuilder getStringValues() {
        final StringBuilder values = new StringBuilder();
        for (Integer num : valueList) {
            values.append(num).append("; ");
        }
        return values;
    }

}
