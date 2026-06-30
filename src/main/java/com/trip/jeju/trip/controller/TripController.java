package com.trip.jeju.trip.controller;

import com.trip.jeju.common.vo.ApiResponse;
import com.trip.jeju.common.vo.PageResVO;
import com.trip.jeju.trip.service.TripService;
import com.trip.jeju.trip.vo.TripSaveReqVO;
import com.trip.jeju.trip.vo.TripSearchReqVO;
import com.trip.jeju.trip.vo.TripVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("trip")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResVO<TripVO>>> getTripList(
            @ModelAttribute TripSearchReqVO reqVO) {
        return ResponseEntity.ok(ApiResponse.ok(tripService.getTripList(reqVO)));
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<ApiResponse<TripVO>> getTripDetail(@PathVariable Integer tripId) {
        return ResponseEntity.ok(ApiResponse.ok(tripService.getTripDetail(tripId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Integer>> createTrip(@RequestBody TripSaveReqVO reqVO) {
        Integer tripId = tripService.createTrip(reqVO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(tripId));
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<ApiResponse<Void>> updateTrip(
            @PathVariable Integer tripId,
            @RequestBody TripSaveReqVO reqVO) {
        tripService.updateTrip(tripId, reqVO);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<ApiResponse<Void>> deleteTrip(@PathVariable Integer tripId) {
        tripService.deleteTrip(tripId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
