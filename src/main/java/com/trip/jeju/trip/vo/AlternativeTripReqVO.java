package com.trip.jeju.trip.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlternativeTripReqVO {

    private String keyword;

    // 현재 보고 있는 관광지
    private String contentId;

    // 분류체계
    private String lclsSystm1;
    private String lclsSystm2;
    private String lclsSystm3;

    // 페이지
    private int pageNo;

    // 혼잡도 기준 날짜
    private String baseYmd;
}