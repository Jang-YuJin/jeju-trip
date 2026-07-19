package com.trip.jeju.trip.service;

import com.trip.jeju.trip.mapper.SearchHistoryMapper;
import com.trip.jeju.trip.vo.SearchHistoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryMapper searchHistoryMapper;

    public void saveSearchHistory(SearchHistoryVO searchHistoryVO) {
        searchHistoryMapper.insertSearchHistory(searchHistoryVO);
    }
}
