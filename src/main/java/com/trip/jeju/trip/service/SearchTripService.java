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
import tools.jackson.databind.node.ObjectNode;

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

    private volatile Map<String, String> lclsNameMap; // 1,2,3단계 코드를 모두 담음 (코드가 서로 겹치지 않음)

    @Value("${ks.key}")
    private String key;

    @Value("${ks.url}")
    private String baseUrl;

    @Value("${api.mobile.os}")
    private String os;

    @Value("${api.mobile.app}")
    private String app;



    public List<TripResVO> searchTrip(Map<String, Object> params, String endpoint){
        long searchStart = System.nanoTime();
        int numOfRows = 500;
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
            long searchElapsedNanos = System.nanoTime() - searchStart;
            log.info("목록 조회 시간 - {}", searchElapsedNanos / 1_000_000 + " ms");
            List<TripResVO> result = new ArrayList<>();

            Map<String, String> lclsMap = getLclsNameMap();

            long start = System.nanoTime();
            for (JsonNode item : items) {
                //분류체계 FD(음식), AC(숙박) 제외, 콘텐츠타입아이디 21(숙박), 39(음식점) 제외
                if("/searchKeyword2".equals(endpoint) && !"FD".equals(item.path("lclsSystm1").asText()) && !"AC".equals(item.path("lclsSystm1").asText()) && !"21".equals(item.path("contenttypeid").asText()) && !"39".equals(item.path("contenttypeid").asText())){
                    if (item instanceof ObjectNode obj) {
                        obj.put("lclsSystm1Nm", lclsMap.getOrDefault(item.path("lclsSystm1").asText(), ""));
                        obj.put("lclsSystm2Nm", lclsMap.getOrDefault(item.path("lclsSystm2").asText(), ""));
                        obj.put("lclsSystm3Nm", lclsMap.getOrDefault(item.path("lclsSystm3").asText(), ""));
                    }
                    result.add(toTripResVO(item, params.get("baseYmd")));
                } else if("/locationBasedList2".equals(endpoint) && !"FD".equals(item.path("lclsSystm1").asText()) && !"AC".equals(item.path("lclsSystm1").asText()) && !"21".equals(item.path("contenttypeid").asText()) && !"39".equals(item.path("contenttypeid").asText())){
                    result.add(toTripResVO(item, params.get("baseYmd")));
                }
            }
            long elapsedNanos = System.nanoTime() - searchStart;
            log.info("분류체계 및 혼잡도 조회 시간 - {}", elapsedNanos / 1_000_000 + " ms");

            return result;

        } catch (Exception e) {
            log.error("한국관광공사_국문 관광정보 서비스_GW API 호출 실패 - {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private TripResVO toTripResVO(JsonNode item, Object baseYmd) {
        try {
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
                    .lclsSystm1Nm(item.path("lclsSystm1Nm").asText())
                    .lclsSystm2(item.path("lclsSystm2").asText())
                    .lclsSystm2Nm(item.path("lclsSystm2Nm").asText())
                    .lclsSystm3(item.path("lclsSystm3").asText())
                    .lclsSystm3Nm(item.path("lclsSystm3Nm").asText())
                    .lDongRegnCd(item.path("lDongRegnCd").asText())
                    .lDongSignguCd(item.path("lDongSignguCd").asText())
                    .congestion(congestion)
                    .build();
        } catch (Exception e) {
            log.error("한국관광공사_국문 관광정보 서비스_GW 분류체계 코드조회 API 호출 실패 - {}", e.getMessage());
            return null;
        }
    }

    private Map<String, String> getLclsNameMap() {
        if (lclsNameMap != null) return lclsNameMap;
        synchronized (this) {
            if (lclsNameMap != null) return lclsNameMap;

            String lclsUrl = UriComponentsBuilder
                    .fromUriString(baseUrl + "/lclsSystmCode2")
                    .queryParam("serviceKey", key)
                    .queryParam("numOfRows", 1000)
                    .queryParam("pageNo", 1)
                    .queryParam("MobileOS", os)
                    .queryParam("MobileApp", app)
                    .queryParam("lclsSystmListYn", "Y")
                    .queryParam("_type", "json")
                    .build(false).toUriString();

            Map<String, String> map = new HashMap<>();
            try {
                String res = restTemplate.getForObject(lclsUrl, String.class);
                JsonNode root = objectMapper.readTree(res);

                if (!"0000".equals(root.path("response").path("header").path("resultCode").asText())) {
                    return map; // 실패 시 캐시하지 않고 다음 호출에서 재시도
                }

                JsonNode rows = root.path("response").path("body").path("items").path("item");
                for (JsonNode r : rows) {
                    putIfPresent(map, r.path("lclsSystm1Cd").asText(), r.path("lclsSystm1Nm").asText());
                    putIfPresent(map, r.path("lclsSystm2Cd").asText(), r.path("lclsSystm2Nm").asText());
                    putIfPresent(map, r.path("lclsSystm3Cd").asText(), r.path("lclsSystm3Nm").asText());
                }
                lclsNameMap = map;
            } catch (Exception e) {
                log.error("분류체계 코드 조회 실패 - {}", e.getMessage());
            }
            return map;
        }
    }

    private void putIfPresent(Map<String, String> map, String code, String name) {
        if (!code.isEmpty() && !name.isEmpty()) map.put(code, name);
    }
}
