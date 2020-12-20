package com.gmail.t_2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gmail.t_2.service.MathService;
import com.gmail.t_2.service.impl.MathServiceImpl;

import java.util.List;

public class SecondActivity extends AppCompatActivity {

    public static final String RESULT_1 = "result1";
    public static final String RESULT_2 = "result2";
    public static final String RESULT_3 = "result3";

    private MathService mathService = new MathServiceImpl();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        if (intent != null) {
            List<Integer> valueList = intent.getIntegerArrayListExtra(MainActivity.VALUE_LIST);

            Double result1 = mathService.getListAverage(valueList);
            Double result2 = mathService.getListSum(valueList);
            Double result3 = mathService.getSumOfFirstHalf(valueList) / mathService.getDifferenceOfLastHalf(valueList);

            Intent resultIntent = new Intent();
            resultIntent.putExtra(RESULT_1, result1);
            resultIntent.putExtra(RESULT_2, result2);
            resultIntent.putExtra(RESULT_3, result3);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}
