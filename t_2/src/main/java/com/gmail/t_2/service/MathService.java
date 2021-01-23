package com.gmail.t_2.service;

import java.util.List;

import dagger.Component;

@Component
public interface MathService {
    Integer getRandom();

    Double getListAverage(List<Integer> valueList);

    Double getListSum(List<Integer> valueList);

    Double getSumOfFirstHalf(List<Integer> valueList);

    Double getDifferenceOfLastHalf(List<Integer> valueList);

    List<Integer> generateNewList();
}
