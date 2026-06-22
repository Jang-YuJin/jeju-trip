package com.trip.jeju.trip.service;

import com.trip.jeju.trip.vo.DetailCommonResVO;
import com.trip.jeju.trip.vo.DetailIntroResVO;
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

    public DetailIntroResVO detailIntro(Map<String, Object> params) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(baseUrl + "/detailIntro2")
                .queryParam("serviceKey", key)
                .queryParam("MobileOS", os)
                .queryParam("MobileApp", app)
                .queryParam("numOfRows", 1)
                .queryParam("pageNo", 1)
                .queryParam("_type", "json");

        params.forEach(builder::queryParam);

        String url = builder.build(false).toUriString();

        log.info("소개정보조회 URL = {}", url);

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            log.debug("소개정보조회 응답 JSON = {}", jsonResponse);

            JsonNode root = objectMapper.readTree(jsonResponse);

            String resultCode = root
                    .path("response")
                    .path("header")
                    .path("resultCode")
                    .asText();

            if (!"0000".equals(resultCode)) {
                String resultMsg = root.path("response").path("header").path("resultMsg").asText();
                log.warn("소개정보조회 API 오류 - resultCode: {}, resultMsg: {}", resultCode, resultMsg);
                return null;
            }

            JsonNode item = root
                    .path("response")
                    .path("body")
                    .path("items")
                    .path("item")
                    .get(0);

            if (item == null || item.isMissingNode()) {
                log.warn("소개정보조회 결과 없음");
                return null;
            }

            return DetailIntroResVO.builder()
                    .contentid(item.path("contentid").asText())
                    .contenttypeid(item.path("contenttypeid").asText())
                    .infocenter(item.path("infocenter").asText())
                    .restdate(item.path("restdate").asText())
                    .usetime(item.path("usetime").asText())
                    .parking(item.path("parking").asText())
                    .chkpet(item.path("chkpet").asText())
                    .expguide(item.path("expguide").asText())
                    .expagerange(item.path("expagerange").asText())
                    .infocentershopping(item.path("infocentershopping").asText())
                    .restdateshopping(item.path("restdateshopping").asText())
                    .parkingshopping(item.path("parkingshopping").asText())
                    .opentime(item.path("opentime").asText())
                    .restroom(item.path("restroom").asText())
                    .saleitem(item.path("saleitem").asText())
                    .build();

        } catch (Exception e) {
            log.error("소개정보조회 API 호출 실패 - {}", e.getMessage());
            return null;
        }
    }
}