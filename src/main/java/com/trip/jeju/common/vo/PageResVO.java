package com.trip.jeju.common.vo;

import lombok.Getter;

import java.util.List;

/**
 * 목록 공통 응답 VO
 * @param <T>
 */
@Getter
public class PageResVO<T> {
    private List<T> content;
    private int totalCount;
    private int pageNo;
    private int numOfRows;
    private int totalPages;

    public PageResVO(List<T> content, int totalCount, int pageNo, int numOfRows) {
        this.content = content;
        this.totalCount = totalCount;
        this.pageNo = pageNo;
        this.numOfRows = numOfRows;
        this.totalPages = (int) Math.ceil((double) totalCount / numOfRows);
    }
}
