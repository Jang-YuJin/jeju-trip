package com.trip.jeju.trip.service;

import com.trip.jeju.trip.vo.DetailCommonResVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetailTripService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${ks.key}")
    private String key;

    @Value("${ks.url}")
    private String baseUrl;

    @Value("${api.mobile.os}")
    private String os;

    @Value("${api.mobile.app}")
    private String app;

    public DetailCommonResVO detailCommon(Map<String, Object> params) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(baseUrl + "/detailCommon2")
                .queryParam("serviceKey", key)
                .queryParam("MobileOS", os)
                .queryParam("MobileApp", app)
                .queryParam("numOfRows", 1)
                .queryParam("pageNo", 1)
                .queryParam("_type", "json");

        params.forEach(builder::queryParam);

        String url = builder.build(false).toUriString();

        log.info("공통정보조회 URL = {}", url);

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            log.debug("공통정보조회 응답 JSON = {}", jsonResponse);

            JsonNode root = objectMapper.readTree(jsonResponse);

            String resultCode = root
                    .path("response")
                    .path("header")
                    .path("resultCode")
                    .asText();

            if (!"0000".equals(resultCode)) {
                String resultMsg = root.path("response").path("header").path("resultMsg").asText();
                log.warn("공통정보조회 API 오류 - resultCode: {}, resultMsg: {}", resultCode, resultMsg);
                return null;
            }

            JsonNode item = root
                    .path("response")
                    .path("body")
                    .path("items")
                    .path("item")
                    .get(0);

            if (item == null || item.isMissingNode()) {
                log.warn("공통정보조회 결과 없음");
                return null;
            }

            return DetailCommonResVO.builder()
                    .contentid(item.path("contentid").asText())
                    .title(item.path("title").asText())
                    .overview(item.path("overview").asText())
                    .homepage(item.path("homepage").asText())
                    .tel(item.path("tel").asText())
                    .addr1(item.path("addr1").asText())
                    .firstimage(item.path("firstimage").asText())
                    .build();

        } catch (Exception e) {
            log.error("공통정보조회 API 호출 실패 - {}", e.getMessage());
            return null;
        }
    }
}