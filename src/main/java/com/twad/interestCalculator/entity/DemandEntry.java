package com.twad.interestCalculator.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class DemandEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int slNo;
    private int year;
    private int month; // 1 = January, etc.
    private int demand;

    public DemandEntry() {
        // required by JPA
    }

    public DemandEntry(int slNo, int year, int month, int demand) {
        this.slNo = slNo;
        this.year = year;
        this.month = month;
        this.demand = demand;
    }

    // getters/setters or Lombok @Data if you prefer
}
