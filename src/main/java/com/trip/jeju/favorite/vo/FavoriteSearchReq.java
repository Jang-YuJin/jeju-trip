package com.trip.jeju.favorite.vo;

import com.trip.jeju.common.vo.PageReqVO;
import io.swagger.v3.oas.annotations.media.Schema;

public class FavoriteSearchReq extends PageReqVO {
    @Schema(description = "사용자 ID(백단에서 세팅되도록 할 것임으로 넣어줄 필요 X)", example = "")
    private String userId;
}
