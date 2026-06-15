package com.trip.jeju.trip.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripReqVO {
    private String keyword;
    private int pageNo;
    private String mapX;
    private String mapY;
    private String radius;
}
