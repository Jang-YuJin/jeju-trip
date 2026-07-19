package com.trip.jeju.trip.mapper;

import com.trip.jeju.trip.vo.SearchHistoryVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SearchHistoryMapper {

    void insertSearchHistory(SearchHistoryVO searchHistoryVO);
}
