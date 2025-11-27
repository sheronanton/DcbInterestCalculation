package com.twad.interestCalculator.service;

import java.util.ArrayList;
import java.util.List;

import com.twad.interestCalculator.InterestResponse;
import com.twad.interestCalculator.entity.DemandEntry;

public class InterestCalculatorService {

	public static class ResultEntry {
	    public int month, year, demand, openingBalance, closingBalance, interest,overdueAmount;

	    public ResultEntry(int month, int year, int demand, int openingBalance, int closingBalance, int interest, int overdueAmount) {
	        this.month = month;
	        this.year = year;
	        this.demand = demand;
	        this.openingBalance = openingBalance;
	        this.closingBalance = closingBalance;
	        this.interest = interest;
	        this.overdueAmount = overdueAmount;
	    }
	}


    
   
	public InterestResponse calculateInterestUrban(List<DemandEntry> demands, String mode) {
	    List<ResultEntry> results = new ArrayList<>();
	    int openingBalance = 0;
	    int totalClosingBalance = 0;
	    int totalInterest = 0;

	    int startMonth;
	    double interestRate;
	    if ("private".equalsIgnoreCase(mode)) {
	        startMonth = 2;
	        interestRate = 2.0; // 2%
	    } else {
	        startMonth = 3;
	        interestRate = 0.5; // 5%
	    }

	    for (int i = 0; i < demands.size(); i++) {
	        DemandEntry entry = demands.get(i);
	        int interest = 0;
	        int overdueBalance = 0;

	        int closingBalance = openingBalance + entry.getDemand();

	        if (i >= startMonth) {
	            if ("localbody".equalsIgnoreCase(mode)) {
	                int removeDemand = demands.get(i - 1).getDemand() + demands.get(i - 2).getDemand();
	                overdueBalance = openingBalance - removeDemand;
	            } else { // private
	                // Only remove ONE prior month's demand
	                int removeDemand = demands.get(i - 1).getDemand();
	                overdueBalance = openingBalance - removeDemand;
	            }
	            int baseDemand = demands.get(i - startMonth).getDemand();
	            interest = (int) Math.round(overdueBalance * interestRate / 100.0);

	        }

	        results.add(new ResultEntry(
	            entry.getMonth(),
	            entry.getYear(),
	            entry.getDemand(),
	            openingBalance,
	            closingBalance,
	            interest,
	            overdueBalance
	        ));

	        openingBalance = closingBalance;
	        totalClosingBalance = closingBalance;
	        totalInterest += interest;
	    }

	    return new InterestResponse(results, totalClosingBalance, totalInterest);
	}

	



}
