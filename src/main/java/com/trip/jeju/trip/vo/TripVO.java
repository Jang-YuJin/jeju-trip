package com.trip.jeju.trip.vo;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripVO {
    private Integer tripId;
    private Integer userId;
    private String tripName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String isAiRoute;
    private String shareCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<TripDetailVO> details;
}
