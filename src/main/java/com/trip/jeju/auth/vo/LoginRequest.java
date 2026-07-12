package com.trip.jeju.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Schema(description = "로그인 요청 VO")
@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {
    @Schema(description = "이메일", example = "test@test.test")
    private String email;

    @Schema(description = "비밀번호", example = "asd123!@#")
    private String password;
}
