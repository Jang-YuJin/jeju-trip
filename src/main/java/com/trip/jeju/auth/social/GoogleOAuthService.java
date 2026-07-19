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
public class GoogleOAuthService implements SocialOAuthService {
    private final RestTemplate restTemplate;

    @Value("${oauth.google.client-id}") private String clientId;
    @Value("${oauth.google.client-secret}") private String clientSecret;
    @Value("${oauth.google.redirect-uri}") private String redirectUri;

    @Override
    public AuthProvider getProvider() { return AuthProvider.GOOGLE; }

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
                "https://oauth2.googleapis.com/token",
                new HttpEntity<>(body, headers),
                JsonNode.class);

        return response.get("access_token").asText();
    }

    private SocialUserInfo fetchProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        JsonNode response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                JsonNode.class).getBody();

        return SocialUserInfo.builder()
                .provider(AuthProvider.GOOGLE)
                .socialId(response.get("id").asText())
                .email(response.get("email").asText())
                .nickname(response.has("name") ? response.get("name").asText() : null)
                .name(response.has("name") ? response.get("name").asText() : null)
                .profileImage(response.has("picture") ? response.get("picture").asText() : null)
                .build();
    }
}
