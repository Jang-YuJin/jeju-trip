package com.trip.jeju.user.controller;

import com.trip.jeju.common.vo.ApiResponse;
import com.trip.jeju.user.service.UserService;
import com.trip.jeju.user.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "User 관련 API")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "JWT_Auth_Token")
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "User 정보 조회", description = "로그인한 User 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<UserVO>> getUser() {
        return ResponseEntity.ok(ApiResponse.ok(userService.getUser()));
    }
}
