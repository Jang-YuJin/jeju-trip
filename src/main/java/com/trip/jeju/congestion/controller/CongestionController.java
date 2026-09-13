package com.trip.jeju.congestion.controller;

import com.trip.jeju.common.vo.ApiResponse;
import com.trip.jeju.congestion.service.CongestionService;
import com.trip.jeju.congestion.vo.CongestionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Tag(name = "Congestion API", description = "혼잡도만 조회하는 API")
@RestController
@RequestMapping("/congestion")
@RequiredArgsConstructor
public class CongestionController {
    private final CongestionService congestionService;

    @Operation(summary = "혼잡도 정보만 조회", description = "혼잡도 정보만 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<CongestionVO>> getCongestion(@RequestParam String areaCd, @RequestParam String spotName, @RequestParam String signguCd) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("areaCd", areaCd);
        param.put("tAtsNm", spotName);
        param.put("signguCd", signguCd);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        congestionService.getCongestion(param)
                )
        );
    }
}
