package com.trip.jeju.trip.mapper;

import com.trip.jeju.trip.vo.SearchHistoryResVO;
import com.trip.jeju.trip.vo.SearchHistoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SearchHistoryMapper {

    // 검색 기록 신규 저장
    void insertSearchHistory(SearchHistoryVO searchHistoryVO);

    // 동일 사용자가 동일 관광지를 조회한 기록이 있는지 확인
    int countSearchHistory(
            @Param("userId") Integer userId,
            @Param("contentId") String contentId
    );

    // 기존 검색 기록 갱신
    void updateSearchHistory(SearchHistoryVO searchHistoryVO);

    // 최근 조회한 장소 조회
    List<SearchHistoryResVO> selectRecentSearchHistory(
            @Param("userId") Integer userId
    );
}