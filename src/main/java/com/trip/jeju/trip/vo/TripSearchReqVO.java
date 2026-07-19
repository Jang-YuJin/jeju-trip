package com.trip.jeju.trip.vo;

import com.trip.jeju.common.vo.PageReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripSearchReqVO extends PageReqVO {
    @Schema(description = "검색 여행 이름", example = "제주도 1박2일")
    private String spotName;

    @Schema(description = "사용자 아이디(내부적으로 넣어줍니다.)", example = "")
    private Integer userId;
}
