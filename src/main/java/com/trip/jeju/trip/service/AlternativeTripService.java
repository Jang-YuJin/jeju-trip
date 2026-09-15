package com.trip.jeju.trip.service;

import com.trip.jeju.trip.vo.AlternativeTripReqVO;
import com.trip.jeju.trip.vo.AlternativeTripResVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlternativeTripService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${ks.key}")
    private String key;

    @Value("${related.api.url}")
    private String baseUrl;

    @Value("${api.mobile.os}")
    private String os;

    @Value("${api.mobile.app}")
    private String app;

    public List<AlternativeTripResVO> getAlternativeTrips(
            AlternativeTripReqVO reqVO
    ) {

        List<AlternativeTripResVO> result = new ArrayList<>();

        try {

            int pageNo = reqVO.getPageNo() <= 0
                    ? 1
                    : reqVO.getPageNo();

            String url = UriComponentsBuilder
                    .fromUriString(baseUrl + "/searchKeyword1")
                    .queryParam("serviceKey", key)
                    .queryParam("pageNo", pageNo)
                    .queryParam("numOfRows", 50)
                    .queryParam("MobileOS", os)
                    .queryParam("MobileApp", app)
                    .queryParam("baseYm", reqVO.getBaseYm())
                    .queryParam("areaCd", reqVO.getAreaCd())
                    .queryParam("signguCd", reqVO.getSignguCd())
                    .queryParam("keyword", reqVO.getKeyword())
                    .queryParam("_type", "json")
                    .build(false)
                    .toUriString();

            String jsonResponse =
                    restTemplate.getForObject(url, String.class);

            JsonNode root =
                    objectMapper.readTree(jsonResponse);

            /*
             * API 응답이
             *
             * {
             *   "header": ...,
             *   "body": ...
             * }
             *
             * 또는
             *
             * {
             *   "response": {
             *      "header": ...,
             *      "body": ...
             *   }
             * }
             *
             * 형태로 오는 경우 모두 대응
             */
            JsonNode responseRoot = root.has("response")
                    ? root.path("response")
                    : root;

            String resultCode = responseRoot
                    .path("header")
                    .path("resultCode")
                    .asText();

            String resultMsg = responseRoot
                    .path("header")
                    .path("resultMsg")
                    .asText();

            if (!"0000".equals(resultCode)
                    && !"00".equals(resultCode)) {

                log.warn(
                        "연관 관광지 API 오류 - resultCode: {}, resultMsg: {}",
                        resultCode,
                        resultMsg
                );

                return result;
            }

            JsonNode items = responseRoot
                    .path("body")
                    .path("items")
                    .path("item");

            if (items.isMissingNode() || items.isNull()) {

                log.info(
                        "연관 관광지 조회 결과 없음 - keyword: {}",
                        reqVO.getKeyword()
                );

                return result;
            }

            /*
             * 음식 / 숙박 제외
             * rlteCtgryLclsNm이 "관광지"인 데이터만 사용
             * 연관순위가 높은 순서대로 최대 3개 반환
             */
            if (items.isArray()) {

                for (JsonNode item : items) {

                    String category =
                            item.path("rlteCtgryLclsNm").asText();

                    if (!"관광지".equals(category)) {
                        continue;
                    }

                    result.add(toResponse(item));

                    if (result.size() >= 3) {
                        break;
                    }
                }

            } else if (items.isObject()) {

                String category =
                        items.path("rlteCtgryLclsNm").asText();

                if ("관광지".equals(category)) {
                    result.add(toResponse(items));
                }
            }

            return result;

        } catch (Exception e) {

            log.error(
                    "한국관광공사 관광지별 연관 관광지 API 호출 실패 - {}",
                    e.getMessage(),
                    e
            );

            return result;
        }
    }

    private AlternativeTripResVO toResponse(JsonNode item) {

        return AlternativeTripResVO.builder()
                .tAtsNm(item.path("tAtsNm").asText())
                .tAtsCd(item.path("tAtsCd").asText())
                .rlteTatsNm(item.path("rlteTatsNm").asText())
                .rlteTatsCd(item.path("rlteTatsCd").asText())
                .rlteRank(item.path("rlteRank").asText())
                .rlteRegnCd(item.path("rlteRegnCd").asText())
                .rlteRegnNm(item.path("rlteRegnNm").asText())
                .rlteSignguCd(item.path("rlteSignguCd").asText())
                .rlteSignguNm(item.path("rlteSignguNm").asText())
                .rlteCtgryLclsNm(
                        item.path("rlteCtgryLclsNm").asText()
                )
                .rlteCtgryMclsNm(
                        item.path("rlteCtgryMclsNm").asText()
                )
                .rlteCtgrySclsNm(
                        item.path("rlteCtgrySclsNm").asText()
                )
                .baseYm(item.path("baseYm").asText())
                .build();
    }
}