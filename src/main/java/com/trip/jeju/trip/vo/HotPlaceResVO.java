package com.trip.jeju.trip.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotPlaceResVO {

    private String baseYm;

    private String areaCd;
    private String areaNm;

    private String signguCd;
    private String signguNm;

    private String hubTatsCd;
    private String hubTatsNm;

    private String hubCtgryLclsNm;
    private String hubCtgryMclsNm;

    private String hubRank;

    private String mapX;
    private String mapY;
}