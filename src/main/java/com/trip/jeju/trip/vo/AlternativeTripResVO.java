package com.trip.jeju.trip.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlternativeTripResVO {

    // 기준 관광지
    private String tAtsNm;
    private String tAtsCd;

    // 연관 관광지
    private String rlteTatsNm;
    private String rlteTatsCd;

    // 연관 순위
    private String rlteRank;

    // 연관 관광지 지역
    private String rlteRegnCd;
    private String rlteRegnNm;
    private String rlteSignguCd;
    private String rlteSignguNm;

    // 연관 관광지 분류
    private String rlteCtgryLclsNm;
    private String rlteCtgryMclsNm;
    private String rlteCtgrySclsNm;

    // 기준연월
    private String baseYm;
}