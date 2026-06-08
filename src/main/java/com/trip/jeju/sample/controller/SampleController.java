package com.trip.jeju.sample.controller;

import com.trip.jeju.common.vo.ApiResponse;
import com.trip.jeju.sample.service.SampleService;
import com.trip.jeju.sample.vo.SampleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sample")
@RequiredArgsConstructor
public class SampleController {
    private final SampleService sampleService;

    /**
     * sample 생성
     * @param sample
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createSample(@RequestBody SampleVO sample) {
        sampleService.create(sample);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(null));
    }

    /**
     * sample 전체 조회
     * @return
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<SampleVO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(sampleService.selectAll()));
    }

    /**
     * sample 단건 조회
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SampleVO>> getOne(@PathVariable int id) {
        return ResponseEntity.ok(ApiResponse.ok(sampleService.selectById(id)));
    }

    /**
     * sample 수정
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable int id,
                                                    @RequestBody SampleVO request) {
        sampleService.update(id, request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable int id) {
        sampleService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
