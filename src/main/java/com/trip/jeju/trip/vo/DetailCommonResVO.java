package com.trip.jeju.trip.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailCommonResVO {

    private String contentid;
    private String contenttypeid;

    private String title;
    private String overview;
    private String homepage;
    private String tel;
    private String addr1;
    private String firstimage;

    private String lDongRegnCd;
    private String lDongSignguCd;

    private String lclsSystm1;
    private String lclsSystm2;
    private String lclsSystm3;

    private String mapx;
    private String mapy;
}