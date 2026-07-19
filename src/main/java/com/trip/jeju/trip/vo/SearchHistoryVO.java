package com.trip.jeju.trip.vo;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistoryVO {

    private Integer historyId;
    private Integer userId;

    private String contentId;
    private String contentTypeId;

    private String spotName;
    private String address;

    private String lDongRegnCd;
    private String lDongSignguCd;

    private String lclsSystm1;
    private String lclsSystm2;
    private String lclsSystm3;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private String keyword;
    private String thumbnail;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}