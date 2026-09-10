package com.trip.jeju.trip.service;

import com.trip.jeju.trip.mapper.SearchHistoryMapper;
import com.trip.jeju.trip.vo.SearchHistoryResVO;
import com.trip.jeju.trip.vo.SearchHistoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryMapper searchHistoryMapper;
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


    public void saveSearchHistory(SearchHistoryVO searchHistoryVO) {

        int count = searchHistoryMapper.countSearchHistory(
                searchHistoryVO.getUserId(),
                searchHistoryVO.getContentId()
        );

        if (count > 0) {
            searchHistoryMapper.updateSearchHistory(searchHistoryVO);
        } else {
            searchHistoryMapper.insertSearchHistory(searchHistoryVO);
        }
    }


    public List<SearchHistoryResVO> getRecentSearchHistory(Integer userId) {

        List<SearchHistoryResVO> histories =
                searchHistoryMapper.selectRecentSearchHistory(userId);

        for (SearchHistoryResVO history : histories) {
            setClassificationNames(history);
        }

        return histories;
    }


    /**
     * 최근 조회 장소의 분류체계 코드로
     * 한국관광공사 분류체계 코드조회 API를 호출하여
     * 분류체계명을 설정
     */
    private void setClassificationNames(
            SearchHistoryResVO history
    ) {

        try {

            String url = UriComponentsBuilder
                    .fromUriString(
                            baseUrl + "/lclsSystmCode2"
                    )
                    .queryParam(
                            "serviceKey",
                            key
                    )
                    .queryParam(
                            "numOfRows",
                            1
                    )
                    .queryParam(
                            "MobileOS",
                            os
                    )
                    .queryParam(
                            "MobileApp",
                            app
                    )
                    .queryParam(
                            "lclsSystm1",
                            history.getLclsSystm1()
                    )
                    .queryParam(
                            "lclsSystm2",
                            history.getLclsSystm2()
                    )
                    .queryParam(
                            "lclsSystm3",
                            history.getLclsSystm3()
                    )
                    .queryParam(
                            "lclsSystmListYn",
                            "Y"
                    )
                    .queryParam(
                            "_type",
                            "json"
                    )
                    .build(false)
                    .toUriString();

            String jsonResponse =
                    restTemplate.getForObject(
                            url,
                            String.class
                    );

            JsonNode root =
                    objectMapper.readTree(
                            jsonResponse
                    );

            String resultCode =
                    root
                            .path("response")
                            .path("header")
                            .path("resultCode")
                            .asText();

            if (!"0000".equals(resultCode)) {

                String resultMsg =
                        root
                                .path("response")
                                .path("header")
                                .path("resultMsg")
                                .asText();

                log.warn(
                        "분류체계명 조회 API 오류 - resultCode: {}, resultMsg: {}",
                        resultCode,
                        resultMsg
                );

                return;
            }

            JsonNode items =
                    root
                            .path("response")
                            .path("body")
                            .path("items")
                            .path("item");

            if (items.isMissingNode()
                    || !items.isArray()
                    || items.isEmpty()) {

                log.warn(
                        "분류체계명 조회 결과 없음 - historyId: {}",
                        history.getHistoryId()
                );

                return;
            }

            JsonNode item = items.get(0);

            history.setLclsSystm1Nm(
                    item
                            .path("lclsSystm1Nm")
                            .asText()
            );

            history.setLclsSystm2Nm(
                    item
                            .path("lclsSystm2Nm")
                            .asText()
            );

            history.setLclsSystm3Nm(
                    item
                            .path("lclsSystm3Nm")
                            .asText()
            );

        } catch (Exception e) {

            log.error(
                    "최근 조회 장소 분류체계명 조회 실패 - historyId: {}, error: {}",
                    history.getHistoryId(),
                    e.getMessage()
            );
        }
    }
}