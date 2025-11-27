package com.twad.interestCalculator;

import java.util.List;

import com.twad.interestCalculator.service.InterestCalculatorService.ResultEntry;

public class InterestResponse {
    public List<ResultEntry> results;
    public int totalClosingBalance;
    public int totalInterest;

    public InterestResponse(List<ResultEntry> results, int totalClosingBalance, int totalInterest) {
        this.results = results;
        this.totalClosingBalance = totalClosingBalance;
        this.totalInterest = totalInterest;
    }
}
