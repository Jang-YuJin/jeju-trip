package com.trip.jeju.auth.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserVO {
    private Integer id;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String passwordHash;
    private String authProvider; // AuthProvider.name()
    private String socialId;
    private String role;         // UserRole.name()
    private String img;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
