package com.trip.jeju.user.mapper;

import com.trip.jeju.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper {
    UserVO selectUserById(Integer id);
}
