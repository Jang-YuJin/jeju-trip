package com.trip.jeju.auth.mapper;

import com.trip.jeju.auth.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserVO findByEmail(String email);
    UserVO findById(Integer id);
    void insertUser(UserVO user);
}
