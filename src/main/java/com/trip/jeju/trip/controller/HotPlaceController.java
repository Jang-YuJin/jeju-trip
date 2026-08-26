package com.trip.jeju.trip.controller;

import com.trip.jeju.common.vo.ApiResponse;
import com.trip.jeju.trip.service.HotPlaceService;
import com.trip.jeju.trip.vo.HotPlaceResVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Hot Place API", description = "메인 핫플 관련 API")
@RestController
@RequestMapping("/hot-place")
@RequiredArgsConstructor
public class HotPlaceController {

    private final HotPlaceService hotPlaceService;

    @Operation(
            summary = "메인 핫플 4개 조회",
            description = "한국관광공사 기초지자체 중심 관광정보 API를 활용해 제주시 2개, 서귀포시 2개의 인기 관광지를 조회합니다."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<HotPlaceResVO>>> getHotPlaces(
            @RequestParam String baseYm
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        hotPlaceService.getHotPlaces(baseYm)
                )
        );
    }
}