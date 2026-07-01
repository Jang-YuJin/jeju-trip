package com.trip.jeju.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private SecurityUtil() {
        // 인스턴스화 방지
    }

    /**
     * 현재 로그인한 사용자의 ID를 반환합니다.
     * 로그인하지 않은 경우 null을 반환합니다.
     */
    public static Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof Integer) {
            return (Integer) principal;
        }

        // anonymousUser 등 예상치 못한 타입인 경우
        return null;
    }

    /**
     * 현재 로그인한 사용자의 ID를 반환합니다.
     * 로그인하지 않은 경우 예외를 던집니다. (인증이 필수인 API에서 사용)
     */
    public static Integer getCurrentUserIdOrThrow() {
        Integer userId = getCurrentUserId();
        if (userId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        return userId;
    }
}
