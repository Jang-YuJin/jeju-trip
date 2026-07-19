package com.trip.jeju.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "소셜 로그인 요청")
public class SocialLoginRequest {
    @Schema(description = "소셜 플랫폼에서 발급받은 인가코드", example = "여기에_인가코드_입력")
    private String code;
}
