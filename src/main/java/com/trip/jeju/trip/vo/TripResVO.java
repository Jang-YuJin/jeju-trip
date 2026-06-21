package com.trip.jeju.trip.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripResVO {
    private String contentid;
    private String zipcode;
    private String addr1;
    private String addr2;
    private String firstimage;
    private String mapx;
    private String mapy;
    private String tel;
    private String title;
    private String lclsSystm1Nm;
    private String lclsSystm2Nm;
    private String lclsSystm3Nm;
}
