package com.trip.jeju.trip.controller;

import com.trip.jeju.common.vo.ApiResponse;
import com.trip.jeju.trip.service.DetailTripService;
import com.trip.jeju.trip.vo.DetailAllResVO;
import com.trip.jeju.trip.vo.DetailCommonResVO;
import com.trip.jeju.trip.vo.DetailIntroResVO;
import com.trip.jeju.trip.vo.DetailReqVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("detail")
@RequiredArgsConstructor
public class DetailTripController {

    private final DetailTripService detailTripService;

    @GetMapping("/common")
    public ResponseEntity<ApiResponse<DetailCommonResVO>> common(
            @ModelAttribute DetailReqVO detailReqVO
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("contentId", detailReqVO.getContentid());

        return ResponseEntity.ok(
                ApiResponse.ok(
                        detailTripService.detailCommon(params)
                )
        );
    }

    @GetMapping("/intro")
    public ResponseEntity<ApiResponse<DetailIntroResVO>> intro(
            @ModelAttribute DetailReqVO detailReqVO
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

    @GetMapping
    public ResponseEntity<ApiResponse<DetailAllResVO>> detail(
            @ModelAttribute DetailReqVO detailReqVO,
            Authentication authentication
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("contentId", detailReqVO.getContentid());
        params.put("contentTypeId", detailReqVO.getContenttypeid());

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