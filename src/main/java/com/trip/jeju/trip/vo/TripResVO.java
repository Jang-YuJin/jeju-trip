package com.trip.jeju.trip.vo;

import com.trip.jeju.congestion.vo.CongestionVO;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripResVO {
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
    private CongestionVO congestion;
}
