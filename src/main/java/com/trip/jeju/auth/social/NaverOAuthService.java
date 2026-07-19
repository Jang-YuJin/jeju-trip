package com.trip.jeju.auth.social;

import com.trip.jeju.auth.vo.AuthProvider;
import com.trip.jeju.auth.vo.SocialUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverOAuthService implements SocialOAuthService {

    private final RestTemplate restTemplate;

    @Value("${oauth.naver.client-id}") private String clientId;
    @Value("${oauth.naver.client-secret}") private String clientSecret;
    @Value("${oauth.naver.redirect-uri}") private String redirectUri;

    @Override
    public AuthProvider getProvider() { return AuthProvider.NAVER; }

    @Override
    public SocialUserInfo getUserInfo(String code) {
        String accessToken = getAccessToken(code);
        return fetchProfile(accessToken);
    }

    private String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        JsonNode response = restTemplate.postForObject(
                "https://nid.naver.com/oauth2.0/token",
                new HttpEntity<>(body, headers),
                JsonNode.class);

        // 토큰 응답 로그
        log.debug("[Naver] 토큰 응답: {}", response);

        return response.get("access_token").asText();
    }

    private SocialUserInfo fetchProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        JsonNode response = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                JsonNode.class).getBody();

        // 프로필 응답 전체 로그 ← 여기서 실제 구조 확인
        log.debug("[Naver] 프로필 응답: {}", response);

        JsonNode account = response.get("response");
        log.debug("[Naver] account: {}", account);

        // null 안전하게 꺼내기
        String socialId = getTextSafely(account, "id");
        String email = getTextSafely(account, "email");
        String nickname = getTextSafely(account, "nickname");
        String name = getTextSafely(account, "name");
        String profileImage = getTextSafely(account, "profile_image");

        if (socialId == null) {
            throw new IllegalStateException("네이버 로그인: socialId를 가져올 수 없습니다. 응답: " + response);
        }

        return SocialUserInfo.builder()
                .provider(AuthProvider.NAVER)
                .socialId(socialId)
                .email(email != null ? email : "naver_" + socialId + "@social.jeju")
                .nickname(nickname)
                .name(name)
                .profileImage(profileImage)
                .build();
    }

    // null 안전하게 JsonNode에서 String 꺼내는 유틸 메소드
    private String getTextSafely(JsonNode node, String fieldName) {
        if (node == null) return null;
        JsonNode field = node.get(fieldName);
        return (field != null && !field.isNull()) ? field.asText() : null;
    }
}