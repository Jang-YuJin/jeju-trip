package com.trip.jeju.auth.social;

import com.trip.jeju.auth.vo.AuthProvider;
import com.trip.jeju.auth.vo.SocialUserInfo;

public interface SocialOAuthService {
    AuthProvider getProvider();
    SocialUserInfo getUserInfo(String code);
}
