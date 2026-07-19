package com.trip.jeju.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 목록 공통 요청 VO
 */
@Getter
@Setter
public class PageReqVO {
    @Schema(description = "페이지 수", example = "1")
    private int pageNo = 1;

    @Schema(description = "한 페이지당 데이터 수", example = "10")
    private int numOfRows = 10;

    // MyBatis에서 LIMIT 계산용 (offset, numOfRows)
    public int getOffset() {
        return (pageNo - 1) * numOfRows;
    }
}
