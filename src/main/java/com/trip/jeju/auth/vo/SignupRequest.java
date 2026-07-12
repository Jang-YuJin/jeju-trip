package com.trip.jeju.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Schema(description = "회원가입 요청 VO")
@RequiredArgsConstructor
@Getter
@Setter
public class SignupRequest {
    @Schema(description = "이메일", example = "test@test.test")
    private String email;

    @Schema(description = "비밀번호", example = "asd123!@#")
    private String password;

    @Schema(description = "닉네임", example = "아무개닉네임")
    private String nickname;

    @Schema(description = "이름", example = "아무개")
    private String name;
}
