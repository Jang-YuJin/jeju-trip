package com.trip.jeju.trip.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailCommonResVO {
    private String contentid;
    private String title;
    private String overview;
    private String homepage;
    private String tel;
    private String addr1;
    private String firstimage;
}