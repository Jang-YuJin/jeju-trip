package com.trip.jeju.trip.controller;

import com.trip.jeju.common.vo.ApiResponse;
import com.trip.jeju.trip.service.SearchTripService;
import com.trip.jeju.trip.vo.TripReqVO;
import com.trip.jeju.trip.vo.TripResVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("search")
@RequiredArgsConstructor
public class SearchTripController {
    private final SearchTripService searchTripService;

    /**
     * 한국관광공사_국문 관광정보 서비스_GW API로 관광지 키워드기반 목록 조회
     * @param tripReqVO
     * @return
     */
    @GetMapping("/keyword")
    public ResponseEntity<ApiResponse<List<TripResVO>>> searchTripKeyword(@ModelAttribute TripReqVO tripReqVO) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNo", tripReqVO.getPageNo());
        params.put("keyword", tripReqVO.getKeyword());
        return ResponseEntity.ok(ApiResponse.ok(searchTripService.searchTrip(params, "/searchKeyword2")));
    }

    /**
     * 한국관광공사_국문 관광정보 서비스_GW API로 관광지 위치기반 목록 조회
     * @param tripReqVO
     * @return
     */
    @GetMapping("/location")
    public ResponseEntity<ApiResponse<List<TripResVO>>> searchTripLocation(@ModelAttribute TripReqVO tripReqVO) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNo", tripReqVO.getPageNo());
        params.put("mapX", tripReqVO.getMapX());
        params.put("mapY", tripReqVO.getMapY());
        params.put("radius", tripReqVO.getRadius());
        return ResponseEntity.ok(ApiResponse.ok(searchTripService.searchTrip(params, "/locationBasedList2")));
    }
}
