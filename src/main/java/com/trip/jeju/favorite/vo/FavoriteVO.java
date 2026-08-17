package com.trip.jeju.favorite.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trip.jeju.congestion.vo.CongestionVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Schema(description = "즐겨찾기 생성 정보")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteVO {
    @Schema(description = "즐겨찾기 ID", example = "생성 시 내부적으로 자동 증가로 넣어주지 않아도 됨")
    private Integer favoriteId;

    @Schema(description = "콘텐츠 ID", example = "")
    private String contentId;

    @Schema(description = "콘텐츠타입 ID", example = "")
    private String contentTypeId;

    @Schema(description = "관광지명", example = "")
    private String spotName;

    @Schema(description = "주소", example = "")
    private String address;

    @Schema(description = "법정동코드", example = "")
    private String ldongRegnCd;

    @Schema(description = "법정동시군구코드", example = "")
    private String ldongSignguCd;

    @Schema(description = "1", example = "")
    private String lclsSystm1;

    @Schema(description = "분류체계명2", example = "")
    private String lclsSystm2;

    @Schema(description = "분류체계명3", example = "")
    private String lclsSystm3;

    @Schema(description = "위도", example = "")
    private BigDecimal latitude;

    @Schema(description = "경도", example = "")
    private BigDecimal longitude;

    @Schema(description = "썸네일", example = "")
    private String thumbnail;

    @Schema(description = "사용자 ID(백단에서 세팅되도록 할 것임으로 넣어줄 필요 X)", example = "")
    private String userId;

    @Schema(description = "혼잡도 정보", example = "")
    private CongestionVO congestion;
}
