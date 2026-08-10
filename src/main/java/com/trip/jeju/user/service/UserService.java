package com.trip.jeju.user.service;

import com.trip.jeju.common.util.SecurityUtil;
import com.trip.jeju.user.mapper.UserInfoMapper;
import com.trip.jeju.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserInfoMapper userMapper;

    public UserVO getUser(){
        Integer userId = SecurityUtil.getCurrentUserId();
        return userMapper.selectUserById(userId);
    }
}
