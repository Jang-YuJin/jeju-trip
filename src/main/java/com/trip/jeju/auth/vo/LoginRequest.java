package com.trip.jeju.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
