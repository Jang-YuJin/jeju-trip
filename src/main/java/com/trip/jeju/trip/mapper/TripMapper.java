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
    int selectTripCountNest(TripSearchReqVO reqVO);
    List<TripVO> selectTripListPre(TripSearchReqVO reqVO);
    int selectTripCountPre(TripSearchReqVO reqVO);
    TripVO selectTripById(@Param("tripId") Integer tripId, @Param("userId") Integer userId);
    void insertTrip(TripVO tripVO);
    void updateTrip(TripVO tripVO);
    void deleteTrip(@Param("tripId") Integer tripId);

    List<TripDetailVO> selectDetailsByTripId(@Param("tripId") Integer tripId);
    void insertDetail(TripDetailVO detailVO);
    void updateDetail(TripDetailVO detailVO);
    void deleteDetailsByTripId(@Param("tripId") Integer tripId);
}
