package com.trip.jeju.sample.service;

import com.trip.jeju.sample.mapper.SampleMapper;
import com.trip.jeju.sample.vo.SampleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SampleService {
    private final SampleMapper sampleMapper;

    public void create(SampleVO sample) {
        sampleMapper.create(sample);
    }

    public SampleVO selectById(int id) {
        return sampleMapper.selectById(id);
    }

    public List<SampleVO> selectAll() {
        return sampleMapper.selectAll();
    }

    public int update(int id, SampleVO sample) {
        sample.setEmpId(id);
        return sampleMapper.update(sample);
    }

    public int delete(int id) {
        return sampleMapper.delete(id);
    }
}
