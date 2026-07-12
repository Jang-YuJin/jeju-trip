package com.trip.jeju.trip.vo;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripDetailVO {
    private Integer detailId;
    private Integer tripId;
    private String contentid;
    private String contenttypeid;
    private String zipcode;
    private String addr1;
    private String addr2;
    private String firstimage;
    private String mapx;
    private String mapy;
    private String tel;
    private String title;
    private String lclsSystm1;
    private String lclsSystm2;
    private String lclsSystm3;
    private String lclsSystm1Nm;
    private String lclsSystm2Nm;
    private String lclsSystm3Nm;
    private String lDongRegnCd;
    private String lDongSignguCd;
    private String visitOrder;
    private String visitDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
