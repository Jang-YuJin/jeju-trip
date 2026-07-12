package com.trip.jeju.trip.service;

import com.trip.jeju.common.util.SecurityUtil;
import com.trip.jeju.common.vo.PageResVO;
import com.trip.jeju.trip.mapper.TripMapper;
import com.trip.jeju.trip.vo.TripDetailVO;
import com.trip.jeju.trip.vo.TripSaveReqVO;
import com.trip.jeju.trip.vo.TripSearchReqVO;
import com.trip.jeju.trip.vo.TripVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TripService {
    private final TripMapper tripMapper;

    public PageResVO<TripVO> getTripList(TripSearchReqVO reqVO) {
        List<TripVO> list = tripMapper.selectTripList(reqVO);
        int totalCount = tripMapper.selectTripCount(reqVO);

        log.info("TRIP 목록 조회 - pageNo: {}, spotName: {}, category: {}, totalCount: {}",
                reqVO.getPageNo(), reqVO.getSpotName(), totalCount);

        return new PageResVO<>(list, totalCount, reqVO.getPageNo(), reqVO.getNumOfRows());
    }

    public TripVO getTripDetail(Integer tripId) {
        TripVO trip = tripMapper.selectTripById(tripId);

        if (trip == null) {
            throw new IllegalArgumentException("존재하지 않는 여행입니다. tripId: " + tripId);
        }

        // 해당 TRIP의 디테일 목록을 같이 조회해서 세팅
        List<TripDetailVO> details = tripMapper.selectDetailsByTripId(tripId);
        trip.setDetails(details);

        return trip;
    }

    @Transactional
    public Integer createTrip(TripSaveReqVO reqVO) {
        Integer userId = SecurityUtil.getCurrentUserId();

        // 1. TRIP 등록
        TripVO trip = TripVO.builder()
                .userId(userId)
                .tripName(reqVO.getTripName())
                .startDate(reqVO.getStartDate())
                .endDate(reqVO.getEndDate())
                .isAiRoute(reqVO.getIsAiRoute() != null ? reqVO.getIsAiRoute() : "N")
                .shareCode(reqVO.getShareCode())
                .build();

        tripMapper.insertTrip(trip);
        Integer tripId = trip.getTripId();  // useGeneratedKeys로 자동 세팅됨

        log.info("TRIP 생성 완료 - tripId: {}, userId: {}", tripId, userId);

        // 2. TRIP_DETAIL이 있으면 함께 등록 (없으면 TRIP만 생성)
        List<TripDetailVO> details = reqVO.getDetails();
        if (details != null && !details.isEmpty()) {
            for (TripDetailVO detail : details) {
                detail.setTripId(tripId);
                tripMapper.insertDetail(detail);
            }
            log.info("TRIP_DETAIL {}건 함께 생성 - tripId: {}", details.size(), tripId);
        }

        return tripId;
    }

    @Transactional
    public void updateTrip(Integer tripId, TripSaveReqVO reqVO) {

        TripVO existingTrip = tripMapper.selectTripById(tripId);
        if (existingTrip == null) {
            throw new IllegalArgumentException("존재하지 않는 여행입니다. tripId: " + tripId);
        }

        // 1. TRIP 정보 수정
        TripVO trip = TripVO.builder()
                .tripId(tripId)
                .tripName(reqVO.getTripName())
                .startDate(reqVO.getStartDate())
                .endDate(reqVO.getEndDate())
                .isAiRoute(reqVO.getIsAiRoute())
                .shareCode(reqVO.getShareCode())
                .build();

        tripMapper.updateTrip(trip);
        log.info("TRIP 정보 수정 완료 - tripId: {}", tripId);

        // 2. details가 넘어온 경우에만 TRIP_DETAIL 갈아끼우기
        List<TripDetailVO> details = reqVO.getDetails();
        if (details != null) {
            // 기존 디테일 전체 삭제
            tripMapper.deleteDetailsByTripId(tripId);

            // 새 디테일 등록
            for (TripDetailVO detail : details) {
                detail.setTripId(tripId);
                tripMapper.insertDetail(detail);
            }
            log.info("TRIP_DETAIL {}건 갈아끼우기 완료 - tripId: {}", details.size(), tripId);
        }
    }

    @Transactional
    public void deleteTrip(Integer tripId) {
        TripVO trip = tripMapper.selectTripById(tripId);
        if (trip == null) {
            throw new IllegalArgumentException("존재하지 않는 여행입니다. tripId: " + tripId);
        }

        // 1. 자식(TRIP_DETAIL) 먼저 삭제
        tripMapper.deleteDetailsByTripId(tripId);

        // 2. 부모(TRIP) 삭제
        tripMapper.deleteTrip(tripId);

        log.info("TRIP 및 TRIP_DETAIL 삭제 완료 - tripId: {}", tripId);
    }
}
