package com.trip.jeju.favorite.service;

import com.trip.jeju.common.util.SecurityUtil;
import com.trip.jeju.common.vo.PageResVO;
import com.trip.jeju.congestion.service.CongestionService;
import com.trip.jeju.congestion.vo.CongestionVO;
import com.trip.jeju.favorite.mapper.FavoriteMapper;
import com.trip.jeju.favorite.vo.FavoriteSearchReq;
import com.trip.jeju.favorite.vo.FavoriteVO;
import com.trip.jeju.trip.vo.TripResVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {
    private final FavoriteMapper favoriteMapper;
    private final CongestionService congestionService;
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

    @Transactional
    public void createFavorite(FavoriteVO reqVO) {
        Integer userId = SecurityUtil.getCurrentUserId();
        reqVO.setUserId(String.valueOf(userId));

        favoriteMapper.createFavorite(reqVO);

        log.info("즐겨찾기 생성 완료 - userId: {}", userId);
    }

    public PageResVO<FavoriteVO> getFavoriteList(FavoriteSearchReq reqVO) {
        Integer userId = SecurityUtil.getCurrentUserId();
        reqVO.setUserId(userId);
        List<FavoriteVO> list = favoriteMapper.selectFavoriteList(reqVO);
        for (FavoriteVO item : list) {
            item = getLclsSystmNm(item);
        }
        int totalCount = favoriteMapper.selectFavoriteCount(reqVO);

        return new PageResVO<>(list, totalCount, reqVO.getPageNo(), reqVO.getNumOfRows());
    }

    @Transactional
    public void deleteFavorite(Integer favoriteId) {
        FavoriteVO favorite = favoriteMapper.selectFavoriteById(favoriteId, SecurityUtil.getCurrentUserId());
        if (favorite == null) {
            throw new IllegalArgumentException("존재하지 않는 즐겨찾기입니다. favoriteId: " + favoriteId);
        }

        favoriteMapper.deleteFavorite(favoriteId);

        log.info("즐겨찾기 삭제 완료 - favoriteId: {}", favoriteId);
    }

    public List<FavoriteVO> getMainFavoriteList() {
        Integer userId = SecurityUtil.getCurrentUserId();
        List<FavoriteVO> list = favoriteMapper.selectMainFavoriteList(userId);

        Map<String, Object> params = new HashMap<>();
        for (FavoriteVO favorite : list) {
            params.put("tAtsNm", favorite.getSpotName());
            params.put("signguCd", favorite.getLdongSignguCd());
            params.put("areaCd", favorite.getLdongRegnCd());
            params.put("baseYmd", "");
            favorite.setCongestion(congestionService.getCongestion(params));
        }
        return favoriteMapper.selectMainFavoriteList(userId);
    }

    private FavoriteVO getLclsSystmNm(FavoriteVO item){
        try {
            String url = UriComponentsBuilder
                    .fromUriString(baseUrl + "/lclsSystmCode2")
                    .queryParam("serviceKey", key)
                    .queryParam("numOfRows", 1)
                    .queryParam("MobileOS", os)
                    .queryParam("MobileApp", app)
                    .queryParam("lclsSystm1", item.getLclsSystm1())
                    .queryParam("lclsSystm2", item.getLclsSystm2())
                    .queryParam("lclsSystm3", item.getLclsSystm3())
                    .queryParam("lclsSystmListYn", "Y")
                    .queryParam("_type", "json")
                    .build(false).toUriString();

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

            item.setLclsSystm1Nm(items.get(0).path("lclsSystm1Nm").asText());
            item.setLclsSystm2Nm(items.get(0).path("lclsSystm2Nm").asText());
            item.setLclsSystm3Nm(items.get(0).path("lclsSystm3Nm").asText());

            return item;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
