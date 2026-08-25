package com.trip.jeju.trip.service;

import com.trip.jeju.trip.vo.AlternativeTripReqVO;
import com.trip.jeju.trip.vo.TripResVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlternativeTripService {

    private final SearchTripService searchTripService;

    public List<TripResVO> getAlternativeTrips(AlternativeTripReqVO reqVO) {

        Map<String, Object> params = new HashMap<>();

        params.put("pageNo", reqVO.getPageNo());
        params.put("keyword", reqVO.getKeyword());
        params.put("baseYmd", reqVO.getBaseYmd());

        if (reqVO.getLclsSystm1() != null && !reqVO.getLclsSystm1().isBlank()) {
            params.put("lclsSystm1", reqVO.getLclsSystm1());
        }

        if (reqVO.getLclsSystm2() != null && !reqVO.getLclsSystm2().isBlank()) {
            params.put("lclsSystm2", reqVO.getLclsSystm2());
        }

        if (reqVO.getLclsSystm3() != null && !reqVO.getLclsSystm3().isBlank()) {
            params.put("lclsSystm3", reqVO.getLclsSystm3());
        }

        List<TripResVO> result =
                searchTripService.searchTrip(params, "/searchKeyword2");

        return result.stream()
                .filter(trip ->
                        reqVO.getContentId() == null
                                || !reqVO.getContentId().equals(trip.getContentid())
                )
                .toList();
    }
}