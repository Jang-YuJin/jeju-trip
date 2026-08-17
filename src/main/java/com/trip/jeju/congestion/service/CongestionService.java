package com.trip.jeju.congestion.service;

import com.trip.jeju.congestion.vo.CongestionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CongestionService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${ks.key}")
    private String key;

    @Value("${tcrs.url}")
    private String baseUrl;

    @Value("${api.mobile.os}")
    private String os;

    @Value("${api.mobile.app}")
    private String app;

    int numOfRows = 30;

    public CongestionVO getCongestion(Map<String, Object> params){
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(baseUrl + "/tatsCnctrRatedList")
                .queryParam("serviceKey", key)
                .queryParam("numOfRows", numOfRows)
                .queryParam("MobileOS", os)
                .queryParam("MobileApp", app)
                .queryParam("_type", "json");

        params.put("signguCd", params.get("areaCd").toString() + params.get("signguCd").toString());
        params.entrySet().stream()
                .filter(entry -> !"baseYmd".equals(entry.getKey()))
                .forEach(entry -> builder.queryParam(entry.getKey(), entry.getValue()));

        String url = builder.build(false).toUriString();
        CongestionVO result = new CongestionVO();
        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
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

            String baseYmd = "";
            if(params.get("baseYmd") != null){
                baseYmd = params.get("baseYmd").toString();
            }else{
                LocalDate date = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                baseYmd = date.format(formatter);
            }
            for (JsonNode item : items) {
                if(item.path("baseYmd").asText().equals(baseYmd)) {
                    result = toCongestionVO(item);
                    break;
                }
            }

        }catch (Exception e) {
            log.error("혼잡도 에러: " + e.getMessage());
            return null;
        }

        return result;
    }

    private CongestionVO toCongestionVO(JsonNode item) {
        return CongestionVO.builder()
                .cnctrRate(item.path("cnctrRate").asText())
                .baseYmd(item.path("baseYmd").asText())
                .areaCd(item.path("areaCd").asText())
                .areaNm(item.path("areaNm").asText())
                .signguCd(item.path("signguCd").asText())
                .signguNm(item.path("signguNm").asText())
                .tAtsNm(item.path("tAtsNm").asText())
                .build();
    }
}
