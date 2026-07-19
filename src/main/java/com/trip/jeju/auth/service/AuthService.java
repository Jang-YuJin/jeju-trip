package com.trip.jeju.auth.service;

import com.trip.jeju.auth.jwt.JwtProvider;
import com.trip.jeju.auth.mapper.UserMapper;
import com.trip.jeju.auth.social.SocialOAuthService;
import com.trip.jeju.auth.social.SocialOAuthServiceFactory;
import com.trip.jeju.auth.vo.*;
import com.trip.jeju.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final SocialOAuthServiceFactory socialOAuthServiceFactory;

    public void signup(SignupRequest req) {
        if (userMapper.findByEmail(req.getEmail()) != null) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }
        UserVO user = new UserVO();
        user.setEmail(req.getEmail());
        user.setNickname(req.getNickname());
        user.setName(req.getName());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setAuthProvider(AuthProvider.LOCAL.name());
        user.setRole(UserRole.MEMBER.name());
        userMapper.insertUser(user);
    }

    public TokenResponse login(LoginRequest req) {
        UserVO user = userMapper.findByEmail(req.getEmail());
        if (user == null || user.getPasswordHash() == null
                || !passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        return issueTokens(user);
    }

    private TokenResponse issueTokens(UserVO user) {
        return new TokenResponse(
                jwtProvider.createAccessToken(user.getId(), user.getRole()),
                jwtProvider.createRefreshToken(user.getId())
        );
    }

    public TokenResponse refresh(RefreshRequest req) {
        String refreshToken = req.getRefreshToken();

        if (!jwtProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException("리프레시 토큰이 유효하지 않습니다.");
        }
        if (!jwtProvider.isRefreshToken(refreshToken)) {
            throw new UnauthorizedException("리프레시 토큰이 아닙니다.");
        }

        Integer userId = jwtProvider.getUserId(refreshToken);
        UserVO user = userMapper.findById(userId);
        if (user == null) {
            throw new UnauthorizedException("존재하지 않는 사용자입니다.");
        }

        return issueTokens(user);
    }

    public TokenResponse socialLogin(AuthProvider provider, String code) {
        SocialOAuthService service = socialOAuthServiceFactory.getService(provider);
        SocialUserInfo info = service.getUserInfo(code);

        // SOCIAL_ID + AUTH_PROVIDER로 기존 유저 조회
        UserVO user = userMapper.findByProviderAndSocialId(provider.name(), info.getSocialId());

        if (user == null) {
            // 신규 유저 → 회원가입 처리
            user = new UserVO();
            user.setEmail(info.getEmail());
            user.setNickname(info.getNickname());
            user.setName(info.getName());
            user.setAuthProvider(provider.name());
            user.setSocialId(info.getSocialId());
            user.setImg(info.getProfileImage());
            user.setRole(UserRole.MEMBER.name());
            // passwordHash는 null (DB 컬럼 NULL 허용으로 변경했으므로)
            userMapper.insertUser(user);
        }

        return issueTokens(user);
    }
}
