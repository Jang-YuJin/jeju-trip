package com.trip.jeju.trip.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailReqVO {
    private String contentId;
    private String contentTypeId;
}