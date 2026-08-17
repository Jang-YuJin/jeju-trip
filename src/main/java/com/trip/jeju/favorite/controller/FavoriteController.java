package com.trip.jeju.favorite.controller;

import com.trip.jeju.common.vo.ApiResponse;
import com.trip.jeju.common.vo.PageResVO;
import com.trip.jeju.favorite.service.FavoriteService;
import com.trip.jeju.favorite.vo.FavoriteSearchReq;
import com.trip.jeju.favorite.vo.FavoriteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "favorite API", description = "즐겨찾기 관련 API")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "JWT_Auth_Token")
@RestController
@RequestMapping("favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @Operation(summary = "즐겨찾기 생성", description = "즐겨찾기를 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Integer>> createFavorite(
            @RequestBody(description = "즐겨찾기 생성 정보", required = true)
            @org.springframework.web.bind.annotation.RequestBody FavoriteVO reqVO) {
        favoriteService.createFavorite(reqVO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(null));
    }

    @Operation(summary = "즐겨찾기 목록 조회", description = "로그인한 회원 아이디로 즐겨찾기 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResVO<FavoriteVO>>> getFavoriteList(
            @ParameterObject @ModelAttribute FavoriteSearchReq reqVO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(favoriteService.getFavoriteList(reqVO)));
    }

    @Operation(summary = "즐겨찾기 삭제", description = "즐겨찾기를 삭제합니다.")
    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<ApiResponse<Void>> deleteFavorite(
            @Parameter(description = "즐겨찾기 ID", example = "1") @PathVariable Integer favoriteId) {
        favoriteService.deleteFavorite(favoriteId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "즐겨찾기 메인 목록 조회", description = "로그인한 회원 아이디로 메인에 즐겨찾기 목록을 조회합니다.(최신순으로 3개 조회)")
    @GetMapping(value = "/main")
    public ResponseEntity<ApiResponse<List<FavoriteVO>>> getMainFavoriteList() {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(favoriteService.getMainFavoriteList()));
    }
}
