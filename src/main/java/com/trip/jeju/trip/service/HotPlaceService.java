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
    private final CongestionService congestionService;

    // 핫플레이스 API
    @Value("${hotplace.api.key}")
    private String key;

    @Value("${hotplace.api.url}")
    private String baseUrl;

    // 국문 관광정보 API
    @Value("${ks.key}")
    private String tourismKey;

    @Value("${ks.url}")
    private String tourismBaseUrl;

    @Value("${api.mobile.os}")
    private String os;

    @Value("${api.mobile.app}")
    private String app;

    public List<HotPlaceResVO> getHotPlaces(String baseYm) {

        List<HotPlaceResVO> result = new ArrayList<>();

        // 제주시 상위 2개
        result.addAll(
                getHotPlacesBySigngu(baseYm, "50110", 2)
        );

        // 서귀포시 상위 2개
        result.addAll(
                getHotPlacesBySigngu(baseYm, "50130", 2)
        );

        return result;
    }

    private List<HotPlaceResVO> getHotPlacesBySigngu(
            String baseYm,
            String signguCd,
            int limit
    ) {

        List<HotPlaceResVO> result = new ArrayList<>();

        /*
         * 제주국제공항 등을 제외하더라도
         * 상위 2개를 채울 수 있도록 후보를 여유 있게 조회
         */
        int candidateCount = 10;

        String url = UriComponentsBuilder
                .fromUriString(baseUrl + "/areaBasedList1")
                .queryParam("serviceKey", key)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", candidateCount)
                .queryParam("MobileOS", os)
                .queryParam("MobileApp", app)
                .queryParam("baseYm", baseYm)
                .queryParam("areaCd", "50")
                .queryParam("signguCd", signguCd)
                .queryParam("_type", "json")
                .build(false)
                .toUriString();

        log.info(
                "기초지자체 중심 관광정보 API 호출 - signguCd: {}, baseYm: {}",
                signguCd,
                baseYm
        );

        try {

            String jsonResponse =
                    restTemplate.getForObject(url, String.class);

            JsonNode root =
                    objectMapper.readTree(jsonResponse);

            String resultCode = root
                    .path("response")
                    .path("header")
                    .path("resultCode")
                    .asText();

            if (!"0000".equals(resultCode)) {

                String resultMsg = root
                        .path("response")
                        .path("header")
                        .path("resultMsg")
                        .asText();

                log.warn(
                        "기초지자체 중심 관광정보 API 오류 - resultCode: {}, resultMsg: {}",
                        resultCode,
                        resultMsg
                );

                return result;
            }

            JsonNode items = root
                    .path("response")
                    .path("body")
                    .path("items")
                    .path("item");

            if (items.isMissingNode() || !items.isArray()) {

                log.warn(
                        "핫플레이스 조회 결과 없음 - signguCd: {}",
                        signguCd
                );

                return result;
            }

            for (JsonNode item : items) {

                String hubTatsNm =
                        item.path("hubTatsNm").asText();

                /*
                 * 제주국제공항 제외
                 */
                if ("제주국제공항".equals(hubTatsNm)) {

                    log.info(
                            "핫플레이스 제외 - {}",
                            hubTatsNm
                    );

                    continue;
                }

                /*
                 * 혼잡도 조회
                 */
                HashMap<String, Object> congestionParam =
                        new HashMap<>();

                congestionParam.put(
                        "areaCd",
                        item.path("areaCd").asText()
                );

                congestionParam.put(
                        "signguCd",
                        item.path("signguCd").asText().substring(2)
                );

                congestionParam.put(
                        "tAtsNm",
                        hubTatsNm
                );

                CongestionVO congestion =
                        congestionService.getCongestion(congestionParam);

                /*
                 * 관광지명으로 국문 관광정보 API를 조회하여
                 * 대표 이미지(firstimage) 가져오기
                 */
                String thumbnail =
                        getThumbnail(hubTatsNm);

                HotPlaceResVO hotPlace =
                        HotPlaceResVO.builder()
                                .baseYm(
                                        item.path("baseYm").asText()
                                )
                                .areaCd(
                                        item.path("areaCd").asText()
                                )
                                .areaNm(
                                        item.path("areaNm").asText()
                                )
                                .signguCd(
                                        item.path("signguCd").asText()
                                )
                                .signguNm(
                                        item.path("signguNm").asText()
                                )
                                .hubTatsCd(
                                        item.path("hubTatsCd").asText()
                                )
                                .hubTatsNm(
                                        hubTatsNm
                                )
                                .hubCtgryLclsNm(
                                        item.path("hubCtgryLclsNm").asText()
                                )
                                .hubCtgryMclsNm(
                                        item.path("hubCtgryMclsNm").asText()
                                )
                                .hubRank(
                                        item.path("hubRank").asText()
                                )
                                .mapX(
                                        item.path("mapX").asText()
                                )
                                .mapY(
                                        item.path("mapY").asText()
                                )
                                .congestion(
                                        congestion
                                )
                                .thumbnail(
                                        thumbnail
                                )
                                .build();

                result.add(hotPlace);

                /*
                 * 공항을 제외한 정상 관광지 2개가 채워지면 종료
                 */
                if (result.size() >= limit) {
                    break;
                }
            }

        } catch (Exception e) {

            log.error(
                    "기초지자체 중심 관광정보 API 호출 실패 - {}",
                    e.getMessage()
            );
        }

        return result;
    }

    /**
     * 관광지명으로 썸네일 조회
     *
     * 1차: 원래 관광지명으로 검색
     * 2차: 검색 실패 시 보정된 키워드로 재검색
     */
    private String getThumbnail(String keyword) {

        String thumbnail =
                searchThumbnail(keyword);

        if (thumbnail != null
                && !thumbnail.isBlank()) {

            return thumbnail;
        }

        String correctedKeyword =
                getCorrectedKeyword(keyword);

        if (!correctedKeyword.equals(keyword)) {

            log.info(
                    "핫플레이스 썸네일 재검색 - 기존: {}, 보정: {}",
                    keyword,
                    correctedKeyword
            );

            thumbnail =
                    searchThumbnail(correctedKeyword);

            if (thumbnail != null
                    && !thumbnail.isBlank()) {

                return thumbnail;
            }
        }

        log.warn(
                "핫플레이스 썸네일 최종 조회 실패 - keyword: {}",
                keyword
        );

        return "";
    }

    /**
     * 관광공사 searchKeyword2 실제 호출
     */
    private String searchThumbnail(String keyword) {

        try {

            String url = UriComponentsBuilder
                    .fromUriString(tourismBaseUrl + "/searchKeyword2")
                    .queryParam("serviceKey", tourismKey)
                    .queryParam("pageNo", 1)
                    .queryParam("numOfRows", 10)
                    .queryParam("MobileOS", os)
                    .queryParam("MobileApp", app)
                    .queryParam("lDongRegnCd", "50")
                    .queryParam("keyword", keyword)
                    .queryParam("_type", "json")
                    .build(false)
                    .toUriString();

            String jsonResponse =
                    restTemplate.getForObject(url, String.class);

            JsonNode root =
                    objectMapper.readTree(jsonResponse);

            String resultCode = root
                    .path("response")
                    .path("header")
                    .path("resultCode")
                    .asText();

            if (!"0000".equals(resultCode)) {

                String resultMsg = root
                        .path("response")
                        .path("header")
                        .path("resultMsg")
                        .asText();

                log.warn(
                        "핫플레이스 썸네일 조회 API 오류 - keyword: {}, resultCode: {}, resultMsg: {}",
                        keyword,
                        resultCode,
                        resultMsg
                );

                return "";
            }

            JsonNode items = root
                    .path("response")
                    .path("body")
                    .path("items")
                    .path("item");

            if (items.isMissingNode()
                    || !items.isArray()
                    || items.isEmpty()) {

                log.warn(
                        "핫플레이스 썸네일 조회 결과 없음 - keyword: {}",
                        keyword
                );

                return "";
            }

            String normalizedKeyword =
                    keyword.replaceAll("\\s+", "");

            for (JsonNode item : items) {

                String title =
                        item.path("title").asText();

                String firstImage =
                        item.path("firstimage").asText();

                String normalizedTitle =
                        title.replaceAll("\\s+", "");

                if (normalizedTitle.equals(normalizedKeyword)
                        && firstImage != null
                        && !firstImage.isBlank()) {

                    return firstImage;
                }
            }

            /*
             * 정확히 같은 이름이 없다면
             * 이미지가 존재하는 첫 번째 검색 결과 사용
             */
            for (JsonNode item : items) {

                String firstImage =
                        item.path("firstimage").asText();

                if (firstImage != null
                        && !firstImage.isBlank()) {

                    return firstImage;
                }
            }

        } catch (Exception e) {

            log.error(
                    "핫플레이스 썸네일 조회 실패 - keyword: {}, error: {}",
                    keyword,
                    e.getMessage()
            );
        }

        return "";
    }

    /**
     * 관광공사 검색용 키워드 보정
     */
    private String getCorrectedKeyword(String keyword) {

        if ("오설록티뮤지엄".equals(keyword)) {
            return "오설록 티 뮤지엄";
        }

        return keyword;
    }
}