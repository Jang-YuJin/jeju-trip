package com.trip.jeju.trip.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "관광지 상세 요청 정보")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailReqVO {
    @Schema(description = "컨텐츠ID", example = "")
    private String contentid;

    @Schema(description = "컨텐츠타입ID", example = "")
    private String contenttypeid;

    @Schema(description = "관광지명", example = "간현관광지")
    private String spotName;

    @Schema(description = "법정동시도코드", example = "50")
    private String areaCd;

    @Schema(description = "법정동시군구코드", example = "130")
    private String signguCd;

    @Schema(description = "조회연월일(YYYYMMDD, 오늘 날짜인 경우 빈 문자열로 보내줘야 함)", example = "20260830")
    private String baseYmd;
}