package com.trip.jeju.trip.controller;

import com.trip.jeju.common.vo.ApiResponse;
import com.trip.jeju.common.vo.PageResVO;
import com.trip.jeju.trip.service.TripService;
import com.trip.jeju.trip.vo.TripSaveReqVO;
import com.trip.jeju.trip.vo.TripSearchReqVO;
import com.trip.jeju.trip.vo.TripVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Trip API", description = "여행 CRUD 관련 API")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "JWT_Auth_Token")
@RestController
@RequestMapping("trip")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;

    @Operation(summary = "여행 목록 조회(전체)", description = "로그인한 회원 아이디로 여행 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResVO<TripVO>>> getTripList(
            @ParameterObject @ModelAttribute TripSearchReqVO reqVO) {
        Integer userId = (Integer) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        reqVO.setUserId(userId);
        return ResponseEntity.ok(ApiResponse.ok(tripService.getTripList(reqVO)));
    }

    @Operation(summary = "여행 목록 조회(진행 예정)", description = "로그인한 회원 아이디로 여행 목록을 조회합니다.")
    @GetMapping(value = "/next")
    public ResponseEntity<ApiResponse<PageResVO<TripVO>>> getTripListNext(
            @ParameterObject @ModelAttribute TripSearchReqVO reqVO) {
        Integer userId = (Integer) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        reqVO.setUserId(userId);
        return ResponseEntity.ok(ApiResponse.ok(tripService.getTripListNext(reqVO)));
    }

    @Operation(summary = "여행 목록 조회(지난 여행)", description = "로그인한 회원 아이디로 여행 목록을 조회합니다.")
    @GetMapping(value = "/pre")
    public ResponseEntity<ApiResponse<PageResVO<TripVO>>> getTripListPre(
            @ParameterObject @ModelAttribute TripSearchReqVO reqVO) {
        Integer userId = (Integer) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        reqVO.setUserId(userId);
        return ResponseEntity.ok(ApiResponse.ok(tripService.getTripListPre(reqVO)));
    }

    @Operation(summary = "여행 상세 조회", description = "로그인한 회원 아이디와 여행ID로 여행 상세를 조회합니다.")
    @GetMapping("/{tripId}")
    public ResponseEntity<ApiResponse<TripVO>> getTripDetail(@Parameter(description = "여행 ID", example = "1") @PathVariable Integer tripId) {
        return ResponseEntity.ok(ApiResponse.ok(tripService.getTripDetail(tripId)));
    }

    @Operation(summary = "여행 생성", description = "여행을 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Integer>> createTrip(
            @RequestBody(description = "여행 생성 정보", required = true)
            @org.springframework.web.bind.annotation.RequestBody TripSaveReqVO reqVO) {
        Integer tripId = tripService.createTrip(reqVO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(tripId));
    }

    @Operation(summary = "여행 수정", description = "여행을 수정합니다.")
    @PutMapping("/{tripId}")
    public ResponseEntity<ApiResponse<Void>> updateTrip(
            @Parameter(description = "여행 ID", example = "1") @PathVariable Integer tripId,
            @RequestBody(description = "여행 수정 정보", required = true)
            @org.springframework.web.bind.annotation.RequestBody TripSaveReqVO reqVO) {
        tripService.updateTrip(tripId, reqVO);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "여행 삭제", description = "여행을 삭제합니다.")
    @DeleteMapping("/{tripId}")
    public ResponseEntity<ApiResponse<Void>> deleteTrip(
            @Parameter(description = "여행 ID", example = "1") @PathVariable Integer tripId) {
        tripService.deleteTrip(tripId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
