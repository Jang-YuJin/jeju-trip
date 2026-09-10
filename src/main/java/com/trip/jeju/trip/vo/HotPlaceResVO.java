package com.trip.jeju.trip.vo;

import com.trip.jeju.congestion.vo.CongestionVO;
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

    // 혼잡도 정보
    private CongestionVO congestion;

    // 관광지 썸네일 이미지
    private String thumbnail;
}