package com.trip.jeju.trip.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TripSaveReqVO {
    private String tripName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String isAiRoute;
    private String shareCode;

    private List<TripDetailVO> details;
}
