package com.trip.jeju.trip.service;

import com.trip.jeju.trip.mapper.SearchHistoryMapper;
import com.trip.jeju.trip.vo.SearchHistoryResVO;
import com.trip.jeju.trip.vo.SearchHistoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryMapper searchHistoryMapper;

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
        return searchHistoryMapper.selectRecentSearchHistory(userId);
    }
}