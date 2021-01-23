package com.gmail.t_2.service.impl;

import com.gmail.t_2.service.MathService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MathServiceImpl implements MathService {

    private Random random;

    @Override
    public Integer getRandom() {
        random = new Random();
        return random.nextInt(1000);
    }

    @Override
    public Double getListAverage(List<Integer> valueList) {
        double avg = 0.0;
        if (valueList != null) {
            for (Integer num : valueList) {
                avg = avg + num;
            }
            avg = avg / valueList.size();
        }
        return avg;
    }

    @Override
    public Double getListSum(List<Integer> valueList) {
        double sum = 0.0;
        if (valueList != null) {
            for (Integer num : valueList) {
                sum = sum + num;
            }
        }
        return sum;
    }

    @Override
    public Double getSumOfFirstHalf(List<Integer> valueList) {
        double sum = 0.0;
        if (valueList != null) {
            int firstHalf = valueList.size() / 2;
            for (int i = 0; i < firstHalf; i++) {
                sum = sum + valueList.get(i);
            }
        }
        return sum;
    }

    @Override
    public Double getDifferenceOfLastHalf(List<Integer> valueList) {
        double difference = 0.0;
        if (valueList != null) {
            int firstHalf = valueList.size() / 2;
            for (int i = firstHalf; i < valueList.size(); i++) {
                difference = difference - valueList.get(i);
            }
        }
        return difference;
    }

    @Override
    public List<Integer> generateNewList() {
        List<Integer> values = new ArrayList<>();
        random = new Random();
        for (int i = 0; i < random.nextInt(5) + 1; i++) {
            values.add(getRandom());
        }
        return values;
    }
}
