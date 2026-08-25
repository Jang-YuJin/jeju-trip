package com.trip.jeju.trip.vo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistoryResVO {

    private Integer historyId;

    private String contentId;
    private String contentTypeId;

    private String spotName;
    private String address;
    private String thumbnail;

    // 분류체계 1/2/3
    private String lclsSystm1;
    private String lclsSystm2;
    private String lclsSystm3;

    private LocalDateTime createdAt;
}