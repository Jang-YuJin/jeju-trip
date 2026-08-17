package com.trip.jeju.trip.controller;

import com.trip.jeju.common.vo.ApiResponse;
import com.trip.jeju.trip.service.DetailTripService;
import com.trip.jeju.trip.vo.DetailAllResVO;
import com.trip.jeju.trip.vo.DetailCommonResVO;
import com.trip.jeju.trip.vo.DetailIntroResVO;
import com.trip.jeju.trip.vo.DetailReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Detail Trip API", description = "관광지 상세 조회 관련 API")
@RestController
@RequestMapping("detail")
@RequiredArgsConstructor
public class DetailTripController {

    private final DetailTripService detailTripService;

    @Operation(
            summary = "관광지 공통정보 조회",
            description = "관광 콘텐츠 ID를 기준으로 관광지의 공통정보를 조회합니다."
    )
    @GetMapping("/common")
    public ResponseEntity<ApiResponse<DetailCommonResVO>> common(
            @ParameterObject @ModelAttribute DetailReqVO detailReqVO
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("contentId", detailReqVO.getContentid());

        return ResponseEntity.ok(
                ApiResponse.ok(
                        detailTripService.detailCommon(params)
                )
        );
    }

    @Operation(
            summary = "관광지 소개정보 조회",
            description = "관광 콘텐츠 ID와 관광 콘텐츠 타입 ID를 기준으로 관광지 소개정보를 조회합니다."
    )
    @GetMapping("/intro")
    public ResponseEntity<ApiResponse<DetailIntroResVO>> intro(
            @ParameterObject @ModelAttribute DetailReqVO detailReqVO
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("contentId", detailReqVO.getContentid());
        params.put("contentTypeId", detailReqVO.getContenttypeid());

        return ResponseEntity.ok(
                ApiResponse.ok(
                        detailTripService.detailIntro(params)
                )
        );
    }

    @Operation(
            summary = "관광지 통합 상세 조회",
            description = "관광 콘텐츠 ID와 관광 콘텐츠 타입 ID를 기준으로 공통정보, 소개정보, 반복정보, 이미지정보를 통합 조회합니다. 로그인한 사용자의 경우 상세조회 기록을 Search History에 저장합니다."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<DetailAllResVO>> detail(
            @ParameterObject @ModelAttribute DetailReqVO detailReqVO,
            @Parameter(hidden = true) Authentication authentication
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("contentId", detailReqVO.getContentid());
        params.put("contentTypeId", detailReqVO.getContenttypeid());
        params.put("tAtsNm", detailReqVO.getSpotName());
        params.put("areaCd", detailReqVO.getAreaCd());
        params.put("signguCd", detailReqVO.getSignguCd());
        params.put("baseYmd", detailReqVO.getBaseYmd());

        // JWT 인증된 사용자인 경우 principal에 userId(Integer)가 들어있음
        Integer userId = null;

        if (authentication != null
                && authentication.getPrincipal() instanceof Integer) {
            userId = (Integer) authentication.getPrincipal();
        }

        return ResponseEntity.ok(
                ApiResponse.ok(
                        detailTripService.detailAll(params, userId)
                )
        );
    }
}