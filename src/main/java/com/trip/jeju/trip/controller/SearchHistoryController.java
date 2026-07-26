package com.trip.jeju.trip.controller;

import com.trip.jeju.common.vo.ApiResponse;
import com.trip.jeju.trip.service.SearchHistoryService;
import com.trip.jeju.trip.vo.SearchHistoryResVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Search History API", description = "최근 조회한 장소 관련 API")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "JWT_Auth_Token")
@RestController
@RequestMapping("search-history")
@RequiredArgsConstructor
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    @Operation(
            summary = "최근 조회한 장소 조회",
            description = "로그인한 사용자의 최근 관광지 상세조회 기록을 최신순으로 조회합니다."
    )
    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<SearchHistoryResVO>>> getRecentSearchHistory(
            Authentication authentication
    ) {
        Integer userId = (Integer) authentication.getPrincipal();

        return ResponseEntity.ok(
                ApiResponse.ok(
                        searchHistoryService.getRecentSearchHistory(userId)
                )
        );
    }
}