package com.trip.jeju.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "User 정보")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    @Schema(description = "이름", example = "아무개")
    private String name;

    @Schema(description = "닉네임", example = "테스트")
    private String nickname;

    @Schema(description = "이메일", example = "test@test.test")
    private String email;

    @Schema(description = "프로필 이미지", example = "https://lh3.googleusercontent.com/a/ACg8ocLS4kXusO2-ZDvh0iV7YY0QSmd9HbWYaAi7VyXKLs9XH8v11A=s96-c")
    private String img;
}
