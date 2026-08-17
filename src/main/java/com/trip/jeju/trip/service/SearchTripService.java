package com.trip.jeju.trip.service;

import com.trip.jeju.congestion.service.CongestionService;
import com.trip.jeju.congestion.vo.CongestionVO;
import com.trip.jeju.trip.vo.TripResVO;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchTripService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CongestionService congestionService;

    @Value("${ks.key}")
    private String key;

    @Value("${ks.url}")
    private String baseUrl;

    @Value("${api.mobile.os}")
    private String os;

    @Value("${api.mobile.app}")
    private String app;

    public List<TripResVO> searchTrip(Map<String, Object> params, String endpoint){
        int numOfRows = 10;
        String lDongRegnCd = "50";
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(baseUrl + endpoint)
                .queryParam("serviceKey", key)
                .queryParam("numOfRows", numOfRows)
                .queryParam("MobileOS", os)
                .queryParam("MobileApp", app)
                .queryParam("lDongRegnCd", lDongRegnCd)
                .queryParam("_type", "json");

        params.entrySet().stream()
                .filter(entry -> !"baseYmd".equals(entry.getKey()))
                .forEach(entry -> builder.queryParam(entry.getKey(), entry.getValue()));

        String url = builder.build(false).toUriString();
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

            List<TripResVO> result = new ArrayList<>();
            for (JsonNode item : items) {
                result.add(toTripResVO(item, params.get("baseYmd")));
            }

            log.info("한국관광공사_국문 관광정보 서비스_GW API 조회 완료 - {}건", result.size());
            return result;

        } catch (Exception e) {
            log.error("한국관광공사_국문 관광정보 서비스_GW API 호출 실패 - {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private TripResVO toTripResVO(JsonNode item, Object baseYmd) {
        try {
            String url = UriComponentsBuilder
                    .fromUriString(baseUrl + "/lclsSystmCode2")
                    .queryParam("serviceKey", key)
                    .queryParam("numOfRows", 1)
                    .queryParam("MobileOS", os)
                    .queryParam("MobileApp", app)
                    .queryParam("lclsSystm1", item.path("lclsSystm1").asText())
                    .queryParam("lclsSystm2", item.path("lclsSystm2").asText())
                    .queryParam("lclsSystm3", item.path("lclsSystm3").asText())
                    .queryParam("lclsSystmListYn", "Y")
                    .queryParam("_type", "json")
                    .build(false).toUriString();
            log.info("한국관광공사_국문 관광정보 서비스_GW 분류체계 코드조회 - url: {}", url);

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
                return null;
            }

            JsonNode items = root
                    .path("response")
                    .path("body")
                    .path("items")
                    .path("item");

            if (items.isMissingNode() || !items.isArray()) {
                log.warn("조회 결과 없음");
                return null;
            }

            Map<String, Object> params = new HashMap<>();
            params.put("areaCd", item.path("lDongRegnCd").asText());
            params.put("signguCd", item.path("lDongSignguCd").asText());
            params.put("tAtsNm", item.path("title").asText());
            if(baseYmd != null && !"".equals(baseYmd)){
                params.put("baseYmd", baseYmd);
            }
            CongestionVO congestion = congestionService.getCongestion(params);

            return TripResVO.builder()
                    .contentid(item.path("contentid").asText())
                    .contenttypeid(item.path("contenttypeid").asText())
                    .zipcode(item.path("zipcode").asText())
                    .addr1(item.path("addr1").asText())
                    .addr2(item.path("addr2").asText())
                    .firstimage(item.path("firstimage").asText())
                    .mapx(item.path("mapx").asText())
                    .mapy(item.path("mapy").asText())
                    .tel(item.path("tel").asText())
                    .title(item.path("title").asText())
                    .lclsSystm1(item.path("lclsSystm1").asText())
                    .lclsSystm2(item.path("lclsSystm2").asText())
                    .lclsSystm3(item.path("lclsSystm3").asText())
                    .lclsSystm1Nm(items.get(0).path("lclsSystm1Nm").asText())
                    .lclsSystm2Nm(items.get(0).path("lclsSystm2Nm").asText())
                    .lclsSystm3Nm(items.get(0).path("lclsSystm3Nm").asText())
                    .lDongRegnCd(item.path("lDongRegnCd").asText())
                    .lDongSignguCd(item.path("lDongSignguCd").asText())
                    .congestion(congestion)
                    .build();
        } catch (Exception e) {
            log.error("한국관광공사_국문 관광정보 서비스_GW 분류체계 코드조회 API 호출 실패 - {}", e.getMessage());
            return null;
        }
    }
}
