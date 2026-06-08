package com.trip.jeju.sample.mapper;

import com.trip.jeju.sample.vo.SampleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SampleMapper {
    void create(SampleVO sample);
    SampleVO selectById(int id);
    List<SampleVO> selectAll();
    int update(SampleVO sample);
    int delete(int id);
}
