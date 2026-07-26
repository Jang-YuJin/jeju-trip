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

    private LocalDateTime createdAt;
}