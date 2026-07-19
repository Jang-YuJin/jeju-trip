package com.trip.jeju.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "토큰 재발행 요청 VO")
@Getter
@Setter
public class RefreshRequest {
    @Schema(description = "재발행 토큰", example = "")
    private String refreshToken;
}
