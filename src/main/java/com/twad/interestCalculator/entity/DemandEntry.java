package com.twad.interestCalculator.entity;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class DemandEntry {
    private int slNo;
    private int year;
    private int month; // 1 for January, 2 for February, etc.
    private int demand;
    
    public DemandEntry(int slNo, int year, int month, int demand) {
        this.slNo = slNo;
        this.year = year;
        this.month = month;
        this.demand = demand;
    }


}
