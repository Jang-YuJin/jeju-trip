package com.trip.jeju.trip.vo;

import com.trip.jeju.congestion.vo.CongestionVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "여행 상세 VO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripDetailVO {
    @Schema(description = "세부 TRIP 아이디", example = "")
    private Integer detailId;

    @Schema(description = "TRIP 아이디", example = "")
    private Integer tripId;

    @Schema(description = "관광지 컨텐츠 아이디", example = "")
    private String contentid;

    @Schema(description = "관광지 콘텐츠 타입 아이디", example = "")
    private String contenttypeid;

    @Schema(description = "우편번호", example = "")
    private String zipcode;

    @Schema(description = "주소1", example = "")
    private String addr1;

    @Schema(description = "주소2", example = "")
    private String addr2;

    @Schema(description = "썸네일이미지", example = "")
    private String firstimage;

    @Schema(description = "위도", example = "")
    private String mapx;

    @Schema(description = "경도", example = "")
    private String mapy;

    @Schema(description = "전화전호", example = "")
    private String tel;

    @Schema(description = "관광지명", example = "")
    private String title;

    @Schema(description = "분류체계 1Deth", example = "")
    private String lclsSystm1;

    @Schema(description = "분류체계 2Deth", example = "")
    private String lclsSystm2;

    @Schema(description = "분류체계 3Deth", example = "")
    private String lclsSystm3;

    @Schema(description = "분류체계 1Deth 명", example = "")
    private String lclsSystm1Nm;

    @Schema(description = "분류체계 2Deth 명", example = "")
    private String lclsSystm2Nm;

    @Schema(description = "분류체계 3Deth 명", example = "")
    private String lclsSystm3Nm;

    @Schema(description = "법정동 시도코드", example = "")
    private String ldongRegnCd;

    @Schema(description = "법정동 시군구코드", example = "")
    private String ldongSignguCd;

    @Schema(description = "방문순서", example = "")
    private String visitOrder;

    @Schema(description = "방문일자", example = "")
    private String visitDate;

    @Schema(description = "생성일자", example = "")
    private LocalDateTime createdAt;

    @Schema(description = "수정일자", example = "")
    private LocalDateTime updatedAt;

    @Schema(description = "혼잡도", example = "")
    private CongestionVO congestion;
}
