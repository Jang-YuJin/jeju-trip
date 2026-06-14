package com.trip.jeju.trip.controller;

import com.trip.jeju.common.vo.ApiResponse;
import com.trip.jeju.trip.service.SearchTripService;
import com.trip.jeju.trip.vo.TripDetailVO;
import com.trip.jeju.trip.vo.TripReqVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("search")
@RequiredArgsConstructor
public class SearchTripController {
    private final SearchTripService searchTripService;

    /**
     * 한국관광공사_국문 관광정보 서비스_GW API로 관광지 목록 조회
     * @param tripReqVO
     * @return
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TripDetailVO>>> searchTrip(@ModelAttribute TripReqVO tripReqVO) {
        return ResponseEntity.ok(ApiResponse.ok(searchTripService.searchTrip(tripReqVO)));
    }
}
