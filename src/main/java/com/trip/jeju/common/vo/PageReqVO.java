package com.trip.jeju.common.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 목록 공통 요청 VO
 */
@Getter
@Setter
public class PageReqVO {
    private int pageNo = 1;
    private int numOfRows = 10;

    // MyBatis에서 LIMIT 계산용 (offset, numOfRows)
    public int getOffset() {
        return (pageNo - 1) * numOfRows;
    }
}
