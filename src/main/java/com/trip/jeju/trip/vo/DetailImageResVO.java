package com.trip.jeju.trip.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailImageResVO {
    private String cpyrhtDivCd;
    private String contentid;
    private String imgname;
    private String originimgurl;
    private String serialnum;
    private String smallimageurl;
}