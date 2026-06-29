package com.trip.jeju.trip.vo;

import com.trip.jeju.common.vo.PageReqVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripSearchReqVO extends PageReqVO {
    private String spotName;
    private String category;
}
