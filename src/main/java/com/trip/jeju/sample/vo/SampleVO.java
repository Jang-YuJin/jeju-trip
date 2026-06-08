package com.trip.jeju.sample.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SampleVO {
    private Integer empId;
    private String name;
    private String department;
    private BigDecimal salary;
    private LocalDate hireDate;
}
