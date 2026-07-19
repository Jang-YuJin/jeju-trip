package com.trip.jeju.auth.controller;

import com.trip.jeju.auth.service.AuthService;
import com.trip.jeju.auth.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "권한 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입", description = "자체 회원가입을 합니다.")
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest req) {
        authService.signup(req);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그인", description = "자체 로그인을 합니다.")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @Operation(summary = "토큰 재발행", description = "토큰을 재발행 합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest req) {
        return ResponseEntity.ok(authService.refresh(req));
    }

    @Operation(summary = "소셜 로그인",
            description = """
                    카카오, 구글, 네이버 소셜 로그인을 처리합니다.
                    
                    **흐름**
                    1. 프론트에서 각 소셜 플랫폼 로그인 후 인가코드(code)를 받음
                    2. 받은 code를 이 API로 전달
                    3. 신규 유저면 자동 회원가입 후 토큰 발급, 기존 유저면 바로 토큰 발급
                    
                    **provider 값**
                    - kakao: 카카오 로그인
                    - google: 구글 로그인
                    - naver: 네이버 로그인
                    
                    **인가코드 받는 URL**
                    - 카카오: `https://kauth.kakao.com/oauth/authorize?client_id={REST_API_KEY}&redirect_uri={REDIRECT_URI}&response_type=code`
                    - 구글: `https://accounts.google.com/o/oauth2/v2/auth?client_id={CLIENT_ID}&redirect_uri={REDIRECT_URI}&response_type=code&scope=openid email profile`
                    - 네이버: `https://nid.naver.com/oauth2.0/authorize?client_id={CLIENT_ID}&redirect_uri={REDIRECT_URI}&response_type=code&state=test`
                    """)
    @PostMapping("/social/{provider}")
    public ResponseEntity<TokenResponse> socialLogin(
            @Parameter(
                    description = "소셜 로그인 제공자 (kakao / google / naver)",
                    example = "kakao",
                    required = true
            ) @PathVariable String provider,
            @RequestBody SocialLoginRequest req) {
        AuthProvider authProvider = AuthProvider.valueOf(provider.toUpperCase());
        return ResponseEntity.ok(authService.socialLogin(authProvider, req.getCode()));
    }
}
