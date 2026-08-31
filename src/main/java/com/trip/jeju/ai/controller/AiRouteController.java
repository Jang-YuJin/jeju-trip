package com.trip.jeju.ai.controller;

import com.trip.jeju.ai.service.AiRouteService;
import com.trip.jeju.common.vo.ApiResponse;
import com.trip.jeju.trip.vo.TripDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "AI Route API",
        description = "Gemini 기반 여행 루트 최적화 API"
)
@RestController
@RequestMapping("/ai/route")
@RequiredArgsConstructor
public class AiRouteController {

    private final AiRouteService aiRouteService;

    @Operation(
            summary = "AI 여행 루트 최적화",
            description = "현재 여행 일정을 Gemini에 전달하여 방문순서를 최적화하고 DB에 저장합니다."
    )
    @PostMapping("/{tripId}")
    public ResponseEntity<ApiResponse<List<TripDetailVO>>> optimizeRoute(
            @PathVariable Integer tripId
    ) {

        List<TripDetailVO> result =
                aiRouteService.optimizeAndSave(tripId);

        return ResponseEntity.ok(
                ApiResponse.ok(result)
        );
    }
}