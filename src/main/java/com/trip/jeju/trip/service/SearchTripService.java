package com.trip.jeju.trip.service;

import com.trip.jeju.trip.vo.TripDetailVO;
import com.trip.jeju.trip.vo.TripReqVO;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchTripService {
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

    public List<TripDetailVO> searchTrip(TripReqVO req){
        int numOfRows = 10;
        String lDongRegnCd = "50";
        String url = UriComponentsBuilder
                .fromUriString(baseUrl + "/searchKeyword2")
                .queryParam("serviceKey", key)
                .queryParam("pageNo", req.getPageNo())
                .queryParam("numOfRows", numOfRows)
                .queryParam("MobileOS", os)
                .queryParam("MobileApp", app)
                .queryParam("lDongRegnCd", lDongRegnCd)
                .queryParam("keyword", req.getKeyword())
                .queryParam("_type", "json")
                .build(false)
                .toUriString();

        log.info("한국관광공사_국문 관광정보 서비스_GW - url: {}", url);

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            log.debug("API 응답 JSON - {}", jsonResponse);

            JsonNode root = objectMapper.readTree(jsonResponse);

            String resultCode = root
                    .path("response")
                    .path("header")
                    .path("resultCode")
                    .asText();

            if (!"0000".equals(resultCode)) {
                String resultMsg = root.path("response").path("header").path("resultMsg").asText();
                log.warn("API 오류 - resultCode: {}, resultMsg: {}", resultCode, resultMsg);
                return Collections.emptyList();
            }

            JsonNode items = root
                    .path("response")
                    .path("body")
                    .path("items")
                    .path("item");

            if (items.isMissingNode() || !items.isArray()) {
                log.warn("조회 결과 없음");
                return Collections.emptyList();
            }

            List<TripDetailVO> result = new ArrayList<>();
            for (JsonNode item : items) {
                result.add(toTripDetailVO(item));
            }

            log.info("한국관광공사_국문 관광정보 서비스_GW API 조회 완료 - {}건", result.size());
            return result;

        } catch (Exception e) {
            log.error("한국관광공사_국문 관광정보 서비스_GW API 호출 실패 - {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private TripDetailVO toTripDetailVO(JsonNode item) {
        return TripDetailVO.builder()
                .spotName(item.path("title").asText())
                .address(item.path("addr1").asText())
                .sigunguCd(item.path("lDongSignguCd").asText())
                .latitude(parseDecimal(item.path("mapy").asText()))   // 위도
                .longitude(parseDecimal(item.path("mapx").asText()))  // 경도
                .category(item.path("cat3").asText())
                .thumnail(item.path("firstimage").asText())
                .build();
    }

    private BigDecimal parseDecimal(String value) {
        if (value == null || value.isBlank()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            log.warn("좌표 변환 실패 - value: {}", value);
            return BigDecimal.ZERO;
        }
    }
}
