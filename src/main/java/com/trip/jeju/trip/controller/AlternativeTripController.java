package com.trip.jeju.trip.controller;

import com.trip.jeju.common.vo.ApiResponse;
import com.trip.jeju.trip.service.AlternativeTripService;
import com.trip.jeju.trip.vo.AlternativeTripReqVO;
import com.trip.jeju.trip.vo.TripResVO;
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

@Tag(name = "Alternative Trip API", description = "대체 관광지 관련 API")
@RestController
@RequestMapping("alternative")
@RequiredArgsConstructor
public class AlternativeTripController {

    private final AlternativeTripService alternativeTripService;

    @Operation(
            summary = "키워드 기반 대체 관광지 조회",
            description = "키워드와 분류체계를 기준으로 대체 관광지를 조회하고 현재 관광지는 제외합니다."
    )
    @GetMapping("/keyword")
    public ResponseEntity<ApiResponse<List<TripResVO>>> getAlternativeTrips(
            @ParameterObject @ModelAttribute AlternativeTripReqVO reqVO
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        alternativeTripService.getAlternativeTrips(reqVO)
                )
        );
    }
}