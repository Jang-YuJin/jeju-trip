package com.trip.jeju.trip.service;

import com.trip.jeju.congestion.service.CongestionService;
import com.trip.jeju.congestion.vo.CongestionVO;
import com.trip.jeju.trip.vo.HotPlaceResVO;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class HotPlaceService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${hotplace.api.key}")
    private String key;

    @Value("${hotplace.api.url}")
    private String baseUrl;

    @Value("${api.mobile.os}")
    private String os;

    @Value("${api.mobile.app}")
    private String app;

    private final CongestionService congestionService;

    public List<HotPlaceResVO> getHotPlaces(String baseYm) {

        List<HotPlaceResVO> result = new ArrayList<>();

        // 제주시 상위 2개
        result.addAll(getHotPlacesBySigngu(baseYm, "50110", 2));

        // 서귀포시 상위 2개
        result.addAll(getHotPlacesBySigngu(baseYm, "50130", 2));

        return result;
    }

    private List<HotPlaceResVO> getHotPlacesBySigngu(
            String baseYm,
            String signguCd,
            int limit
    ) {

        List<HotPlaceResVO> result = new ArrayList<>();

        String url = UriComponentsBuilder
                .fromUriString(baseUrl + "/areaBasedList1")
                .queryParam("serviceKey", key)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", limit)
                .queryParam("MobileOS", os)
                .queryParam("MobileApp", app)
                .queryParam("baseYm", baseYm)
                .queryParam("areaCd", "50")
                .queryParam("signguCd", signguCd)
                .queryParam("_type", "json")
                .build(false)
                .toUriString();

        log.info("기초지자체 중심 관광정보 API - url: {}", url);

        try {

            String jsonResponse =
                    restTemplate.getForObject(url, String.class);

            JsonNode root = objectMapper.readTree(jsonResponse);

            String resultCode = root
                    .path("response")
                    .path("header")
                    .path("resultCode")
                    .asText();

            if (!"0000".equals(resultCode)) {
                log.warn(
                        "기초지자체 중심 관광정보 API 오류 - resultCode: {}",
                        resultCode
                );
                return result;
            }

            JsonNode items = root
                    .path("response")
                    .path("body")
                    .path("items")
                    .path("item");

            if (!items.isArray()) {
                return result;
            }

            HashMap<String, Object> congestionParam = new HashMap<>();
            for (JsonNode item : items) {
                congestionParam.put("areaCd", item.path("areaCd").asText());
                congestionParam.put("signguCd", item.path("signguCd").asText().substring(2));
                congestionParam.put("tAtsNm", item.path("hubTatsNm").asText());

                CongestionVO congestion = congestionService.getCongestion(congestionParam);

                result.add(
                        HotPlaceResVO.builder()
                                .baseYm(item.path("baseYm").asText())
                                .areaCd(item.path("areaCd").asText())
                                .areaNm(item.path("areaNm").asText())
                                .signguCd(item.path("signguCd").asText())
                                .signguNm(item.path("signguNm").asText())
                                .hubTatsCd(item.path("hubTatsCd").asText())
                                .hubTatsNm(item.path("hubTatsNm").asText())
                                .hubCtgryLclsNm(item.path("hubCtgryLclsNm").asText())
                                .hubCtgryMclsNm(item.path("hubCtgryMclsNm").asText())
                                .hubRank(item.path("hubRank").asText())
                                .mapX(item.path("mapX").asText())
                                .mapY(item.path("mapY").asText())
                                .congestion(congestion)
                                .build()
                );
            }

        } catch (Exception e) {
            log.error(
                    "기초지자체 중심 관광정보 API 호출 실패 - {}",
                    e.getMessage()
            );
        }

        return result;
    }
}