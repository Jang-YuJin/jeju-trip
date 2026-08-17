package com.trip.jeju.trip.vo;

import com.trip.jeju.congestion.vo.CongestionVO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailAllResVO {
    private DetailCommonResVO common;
    private DetailIntroResVO intro;
    private List<DetailInfoResVO> info;
    private List<DetailImageResVO> image;
    private CongestionVO congestion;
}