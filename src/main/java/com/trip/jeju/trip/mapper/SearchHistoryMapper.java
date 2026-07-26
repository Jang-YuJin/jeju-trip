package com.trip.jeju.trip.mapper;

import com.trip.jeju.trip.vo.SearchHistoryResVO;
import com.trip.jeju.trip.vo.SearchHistoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SearchHistoryMapper {

    void insertSearchHistory(SearchHistoryVO searchHistoryVO);

    List<SearchHistoryResVO> selectRecentSearchHistory(
            @Param("userId") Integer userId
    );
}