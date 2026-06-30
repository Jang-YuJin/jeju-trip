package com.trip.jeju.auth.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class SignupRequest {
    private String email;
    private String password;
    private String nickname;
    private String name;
}
