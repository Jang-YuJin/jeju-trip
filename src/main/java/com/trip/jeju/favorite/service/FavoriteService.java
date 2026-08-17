package com.trip.jeju.favorite.service;

import com.trip.jeju.common.util.SecurityUtil;
import com.trip.jeju.common.vo.PageResVO;
import com.trip.jeju.congestion.service.CongestionService;
import com.trip.jeju.favorite.mapper.FavoriteMapper;
import com.trip.jeju.favorite.vo.FavoriteSearchReq;
import com.trip.jeju.favorite.vo.FavoriteVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
