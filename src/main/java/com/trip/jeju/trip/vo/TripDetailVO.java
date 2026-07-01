package com.trip.jeju.trip.vo;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripDetailVO {
    private Integer detailId;
    private Integer tripId;
    private String contentId;
    private String contentTypeId;
    private String spotName;
    private String category;
    private String address;
    private String sigunguCd;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime visitTime;
    private LocalDate visitDate;
    private Integer estimatedStay;
    private String thumnail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
