package com.trip.jeju.ai.service;

import com.trip.jeju.ai.vo.AiRouteOrderVO;
import com.trip.jeju.common.util.SecurityUtil;
import com.trip.jeju.trip.mapper.TripMapper;
import com.trip.jeju.trip.vo.TripDetailVO;
import com.trip.jeju.trip.vo.TripVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiRouteService {

    private final TripMapper tripMapper;
    private final GeminiService geminiService;

    @Transactional
    public List<TripDetailVO> optimizeAndSave(Integer tripId) {

        Integer userId = SecurityUtil.getCurrentUserId();

        // 1. 본인 여행인지 확인
        TripVO trip = tripMapper.selectTripById(tripId, userId);

        if (trip == null) {
            throw new IllegalArgumentException(
                    "존재하지 않거나 접근할 수 없는 여행입니다. tripId: " + tripId
            );
        }

        // 2. 현재 여행 상세 일정 조회
        List<TripDetailVO> details =
                tripMapper.selectDetailsByTripId(tripId);

        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException(
                    "최적화할 여행 일정이 없습니다."
            );
        }

        // 3. Gemini에 루트 최적화 요청
        List<AiRouteOrderVO> optimized =
                geminiService.optimizeRoute(details);

        // 4. Gemini 결과 검증
        validateResult(details, optimized);

        // 5. 최적화된 방문 순서 DB 저장
        for (AiRouteOrderVO item : optimized) {
            tripMapper.updateVisitOrder(
                    item.getDetailId(),
                    item.getVisitOrder()
            );
        }

        // 6. AI 루트 적용 여부 Y로 변경
        tripMapper.updateAiRoute(tripId, "Y");

        log.info(
                "AI 여행 루트 최적화 및 저장 완료 - tripId: {}, 관광지 수: {}",
                tripId,
                optimized.size()
        );

        // 7. 저장된 결과 다시 조회
        return tripMapper.selectDetailsByTripId(tripId);
    }


    /**
     * Gemini 결과 검증
     *
     * 1. 기존 관광지 개수와 동일한지
     * 2. 기존에 없는 detailId가 추가되지 않았는지
     * 3. detailId가 중복되거나 누락되지 않았는지
     * 4. visitOrder가 숫자이며 1 이상인지
     * 5. 각 visitDate별 visitOrder가 1부터 N까지 정상적으로 존재하는지
     */
    private void validateResult(
            List<TripDetailVO> original,
            List<AiRouteOrderVO> optimized
    ) {

        // 결과 자체가 없거나 관광지 개수가 달라진 경우
        if (optimized == null
                || original.size() != optimized.size()) {

            throw new IllegalArgumentException(
                    "AI 루트 결과의 관광지 개수가 기존 일정과 일치하지 않습니다."
            );
        }

        // 기존 detailId 목록
        Set<Integer> originalIds = new HashSet<>();

        // detailId -> visitDate 저장
        Map<Integer, String> visitDateByDetailId = new HashMap<>();

        for (TripDetailVO detail : original) {

            originalIds.add(detail.getDetailId());

            visitDateByDetailId.put(
                    detail.getDetailId(),
                    detail.getVisitDate()
            );
        }

        // AI가 반환한 detailId 목록
        Set<Integer> optimizedIds = new HashSet<>();

        // 날짜별 visitOrder 목록
        Map<String, List<Integer>> ordersByDate = new HashMap<>();

        for (AiRouteOrderVO item : optimized) {

            // detailId 확인
            if (item.getDetailId() == null) {
                throw new IllegalArgumentException(
                        "AI 루트 결과에 detailId가 없습니다."
                );
            }

            // 기존 일정에 없는 관광지인지 확인
            if (!originalIds.contains(item.getDetailId())) {
                throw new IllegalArgumentException(
                        "AI가 기존 일정에 없는 관광지를 반환했습니다. detailId: "
                                + item.getDetailId()
                );
            }

            // 중복 관광지 확인
            if (!optimizedIds.add(item.getDetailId())) {
                throw new IllegalArgumentException(
                        "AI 루트 결과에 중복된 관광지가 있습니다. detailId: "
                                + item.getDetailId()
                );
            }

            // visitOrder 존재 여부 확인
            if (item.getVisitOrder() == null
                    || item.getVisitOrder().isBlank()) {

                throw new IllegalArgumentException(
                        "AI 루트 결과에 visitOrder가 없습니다. detailId: "
                                + item.getDetailId()
                );
            }

            int visitOrder;

            // visitOrder 숫자 여부 확인
            try {
                visitOrder = Integer.parseInt(
                        item.getVisitOrder()
                );
            } catch (NumberFormatException e) {

                throw new IllegalArgumentException(
                        "AI 루트 결과의 visitOrder가 숫자가 아닙니다. detailId: "
                                + item.getDetailId()
                );
            }

            // visitOrder는 반드시 1 이상
            if (visitOrder < 1) {
                throw new IllegalArgumentException(
                        "AI 루트 결과의 visitOrder는 1 이상이어야 합니다. detailId: "
                                + item.getDetailId()
                );
            }

            // 기존 일정의 visitDate 확인
            String visitDate =
                    visitDateByDetailId.get(item.getDetailId());

            if (visitDate == null || visitDate.isBlank()) {
                throw new IllegalArgumentException(
                        "기존 일정에 visitDate가 없습니다. detailId: "
                                + item.getDetailId()
                );
            }

            // 날짜별 visitOrder 저장
            ordersByDate
                    .computeIfAbsent(
                            visitDate,
                            key -> new ArrayList<>()
                    )
                    .add(visitOrder);
        }

        // 누락된 관광지 확인
        if (!originalIds.equals(optimizedIds)) {
            throw new IllegalArgumentException(
                    "AI 루트 결과에 누락된 관광지가 있습니다."
            );
        }

        // 날짜별 visitOrder 검증
        for (Map.Entry<String, List<Integer>> entry
                : ordersByDate.entrySet()) {

            String visitDate = entry.getKey();
            List<Integer> orders = entry.getValue();

            // 정렬
            orders.sort(Integer::compareTo);

            /*
             * 관광지가 3개라면
             * 반드시 [1, 2, 3]이어야 함
             */
            for (int i = 0; i < orders.size(); i++) {

                int expectedOrder = i + 1;
                int actualOrder = orders.get(i);

                if (actualOrder != expectedOrder) {
                    throw new IllegalArgumentException(
                            "AI 루트 결과의 방문 순서가 올바르지 않습니다. "
                                    + "visitDate: " + visitDate
                    );
                }
            }
        }
    }
}