package com.trip.jeju.ai.controller;

import com.trip.jeju.ai.service.GeminiService;
import com.trip.jeju.common.vo.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Gemini API", description = "Gemini AI 연동 API")
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @Operation(
            summary = "Gemini 질문 테스트",
            description = "Gemini API에 프롬프트를 전달하고 AI 응답을 반환합니다."
    )
    @PostMapping("/gemini")
    public ResponseEntity<ApiResponse<String>> generateContent(
            @RequestBody Map<String, String> request
    ) {

        String prompt = request.get("prompt");

        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("prompt는 필수입니다.");
        }

        String response = geminiService.generateContent(prompt);

        return ResponseEntity.ok(
                ApiResponse.ok(response)
        );
    }
}