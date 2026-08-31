package com.trip.jeju.trip.mapper;

import com.trip.jeju.trip.vo.TripDetailVO;
import com.trip.jeju.trip.vo.TripSearchReqVO;
import com.trip.jeju.trip.vo.TripVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TripMapper {

    List<TripVO> selectTripList(TripSearchReqVO reqVO);

    int selectTripCount(TripSearchReqVO reqVO);

    List<TripVO> selectTripListNext(TripSearchReqVO reqVO);

    int selectTripCountNext(TripSearchReqVO reqVO);

    List<TripVO> selectTripListPre(TripSearchReqVO reqVO);

    int selectTripCountPre(TripSearchReqVO reqVO);

    TripVO selectTripById(
            @Param("tripId") Integer tripId,
            @Param("userId") Integer userId
    );

    void insertTrip(TripVO tripVO);

    void updateTrip(TripVO tripVO);

    void deleteTrip(
            @Param("tripId") Integer tripId
    );

    List<TripDetailVO> selectDetailsByTripId(
            @Param("tripId") Integer tripId
    );

    void insertDetail(TripDetailVO detailVO);

    void updateDetail(TripDetailVO detailVO);

    // AI 루트 최적화 방문순서 수정
    void updateVisitOrder(
            @Param("detailId") Integer detailId,
            @Param("visitOrder") String visitOrder
    );

    // AI 루트 적용 여부 수정
    void updateAiRoute(
            @Param("tripId") Integer tripId,
            @Param("isAiRoute") String isAiRoute
    );

    void deleteDetailsByTripId(
            @Param("tripId") Integer tripId
    );
}