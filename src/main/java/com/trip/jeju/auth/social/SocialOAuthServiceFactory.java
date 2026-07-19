package com.trip.jeju.auth.social;

import com.trip.jeju.auth.vo.AuthProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SocialOAuthServiceFactory {
    private final Map<AuthProvider, SocialOAuthService> services;

    public SocialOAuthServiceFactory(List<SocialOAuthService> serviceList) {
        this.services = serviceList.stream()
                .collect(Collectors.toMap(SocialOAuthService::getProvider, s -> s));
    }

    public SocialOAuthService getService(AuthProvider provider) {
        SocialOAuthService service = services.get(provider);
        if (service == null) {
            throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다: " + provider);
        }
        return service;
    }
}
