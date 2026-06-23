package com.trip.jeju.auth.service;

import com.trip.jeju.auth.jwt.JwtProvider;
import com.trip.jeju.auth.mapper.UserMapper;
import com.trip.jeju.auth.vo.*;
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

    public void signup(SignupRequest req) {
        if (userMapper.findByEmail(req.getEmail()) != null) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }
        UserVO user = new UserVO();
        user.setEmail(req.getEmail());
        user.setNickname(req.getNickname());
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
}
