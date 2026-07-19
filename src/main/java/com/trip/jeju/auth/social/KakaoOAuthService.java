package com.trip.jeju.auth.social;

import com.trip.jeju.auth.vo.AuthProvider;
import com.trip.jeju.auth.vo.SocialUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService implements SocialOAuthService{
    private final RestTemplate restTemplate;

    @Value("${oauth.kakao.client-id}") private String clientId;
    @Value("${oauth.kakao.client-secret}") private String clientSecret;
    @Value("${oauth.kakao.redirect-uri}") private String redirectUri;

    @Override
    public AuthProvider getProvider() { return AuthProvider.KAKAO; }

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
                "https://kauth.kakao.com/oauth/token",
                new HttpEntity<>(body, headers),
                JsonNode.class);

        return response.get("access_token").asText();
    }

    private SocialUserInfo fetchProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        JsonNode response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                JsonNode.class).getBody();

        JsonNode kakaoAccount = response.get("kakao_account");
        JsonNode profile = kakaoAccount.get("profile");
        String socialId = response.get("id").asText();

        // 카카오는 비즈니스 인증 전엔 이메일 안 줄 수도 있음
        String email = kakaoAccount.has("email")
                ? kakaoAccount.get("email").asText()
                : "kakao_" + socialId + "@social.jeju";

        String nickname = profile.has("nickname")
                ? profile.get("nickname").asText() : null;
        String img = profile.has("profile_image_url")
                ? profile.get("profile_image_url").asText() : null;

        return SocialUserInfo.builder()
                .provider(AuthProvider.KAKAO)
                .socialId(socialId)
                .email(email)
                .nickname(nickname)
                .name(nickname)  // 카카오는 실명 제공 안 함, nickname으로 대체
                .profileImage(img)
                .build();
    }
}
