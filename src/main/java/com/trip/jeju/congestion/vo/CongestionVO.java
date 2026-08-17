package com.trip.jeju.congestion.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "혼잡도 정보")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CongestionVO {
    @Schema(description = "집중률", example = "30.07")
    private String cnctrRate;

    @Schema(description = "기준연월일", example = "20260811")
    private String baseYmd;

    @Schema(description = "지역코드", example = "51")
    private String areaCd;

    @Schema(description = "지역명", example = "강원특별자치도")
    private String areaNm;

    @Schema(description = "시군구코드", example = "51130")
    private String signguCd;

    @Schema(description = "시군구명", example = "원주시")
    private String signguNm;

    @Schema(description = "관광지명", example = "간현관광지")
    private String tAtsNm;
}
