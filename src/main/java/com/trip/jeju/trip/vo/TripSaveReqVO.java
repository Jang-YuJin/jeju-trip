package com.trip.jeju.trip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "여행 생성 정보")
@Getter
@Setter
public class TripSaveReqVO {
    @Schema(description = "여행 이름", example = "제주도 1박2일")
    private String tripName;

    @Schema(description = "여행 시작일", example = "2026-08-01")
    private LocalDate startDate;

    @Schema(description = "여행 종료일", example = "2026-08-02")
    private LocalDate endDate;

    @Schema(description = "AI루트 여부", example = "N")
    private String isAiRoute;

    @Schema(description = "공유 코드", example = "")
    private String shareCode;

    @Schema(description = "여행 상세 리스트")
    private List<TripDetailVO> details;
}
