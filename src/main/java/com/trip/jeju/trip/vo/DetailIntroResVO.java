package com.trip.jeju.trip.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailIntroResVO {
    private String contentid;
    private String contenttypeid;
    private String infocenter;
    private String restdate;
    private String usetime;
    private String parking;
    private String chkpet;
    private String expguide;
    private String expagerange;

    private String infocentershopping;
    private String restdateshopping;
    private String parkingshopping;
    private String opentime;
    private String restroom;
    private String saleitem;
}
