package com.trip.jeju.auth.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialUserInfo {
    private AuthProvider provider;
    private String socialId;
    private String email;
    private String nickname;
    private String name;
    private String profileImage;
}
