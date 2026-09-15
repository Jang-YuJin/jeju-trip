package com.trip.jeju.trip.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlternativeTripReqVO {

    // 현재 보고 있는 관광지명
    private String keyword;

    // 기존 호환용
    private String contentId;
    private String lclsSystm1;
    private String lclsSystm2;
    private String lclsSystm3;

    // 페이지
    private int pageNo;

    // 기존 혼잡도 기준 날짜
    private String baseYmd;

    // 연관 관광지 API 기준연월 (YYYYMM)
    private String baseYm;

    // 연관 관광지 API 지역코드
    private String areaCd;

    // 연관 관광지 API 시군구코드
    private String signguCd;
}