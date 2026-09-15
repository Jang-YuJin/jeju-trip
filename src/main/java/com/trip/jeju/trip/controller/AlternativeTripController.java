package com.trip.jeju.trip.controller;

import com.trip.jeju.common.vo.ApiResponse;
import com.trip.jeju.trip.service.AlternativeTripService;
import com.trip.jeju.trip.vo.AlternativeTripReqVO;
import com.trip.jeju.trip.vo.AlternativeTripResVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Alternative Trip API",
        description = "대체 관광지 관련 API"
)
@RestController
@RequestMapping("alternative")
@RequiredArgsConstructor
public class AlternativeTripController {

    private final AlternativeTripService alternativeTripService;

    @Operation(
            summary = "연관 관광지 기반 대체 관광지 조회",
            description = "한국관광공사 관광지별 연관 관광지 정보 API를 이용하여 대체 관광지를 조회합니다."
    )
    @GetMapping("/keyword")
    public ResponseEntity<ApiResponse<List<AlternativeTripResVO>>> getAlternativeTrips(
            @ParameterObject
            @ModelAttribute AlternativeTripReqVO reqVO
    ) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        alternativeTripService.getAlternativeTrips(reqVO)
                )
        );
    }
}