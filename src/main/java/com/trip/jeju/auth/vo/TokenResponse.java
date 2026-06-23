package com.trip.jeju.auth.vo;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
