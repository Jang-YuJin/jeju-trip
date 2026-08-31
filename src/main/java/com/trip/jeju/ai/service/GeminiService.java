package com.trip.jeju.ai.service;

import com.trip.jeju.ai.vo.AiRouteOrderVO;
import com.trip.jeju.trip.vo.TripDetailVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String baseUrl;

    @Value("${gemini.api.model}")
    private String model;


    /**
     * 기본 Gemini 질문 API
     */
    public String generateContent(String prompt) {

        String url = baseUrl
                + "/models/"
                + model
                + ":generateContent";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(requestBody, headers);

        try {
            String response = restTemplate.postForObject(
                    url,
                    request,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response);

            return root
                    .path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            log.error("Gemini API 호출 실패 - {}", e.getMessage());
            throw new RuntimeException("Gemini API 호출에 실패했습니다.");
        }
    }


    /**
     * AI 여행 루트 최적화
     *
     * 현재 TRIP_DETAIL 목록을 Gemini에 전달하고
     * 효율적인 방문순서를 반환받는다.
     */
    public List<AiRouteOrderVO> optimizeRoute(List<TripDetailVO> details) {

        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("최적화할 여행 일정이 없습니다.");
        }

        String prompt = buildRoutePrompt(details);

        log.info("Gemini 여행 루트 최적화 요청 - 관광지 수: {}", details.size());

        String aiResponse = generateContent(prompt);

        log.debug("Gemini 여행 루트 최적화 응답 - {}", aiResponse);

        return parseRouteResponse(aiResponse);
    }


    /**
     * Gemini에 전달할 여행 루트 최적화 프롬프트 생성
     */
    private String buildRoutePrompt(List<TripDetailVO> details) {

        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                당신은 제주도 여행 동선 최적화 전문가입니다.

                아래 여행 일정에 포함된 관광지들의 위치 좌표를 참고하여
                이동 동선이 최대한 효율적이도록 방문 순서를 정해주세요.

                반드시 다음 규칙을 지켜주세요.

                1. 제공된 관광지를 추가하거나 삭제하지 마세요.
                2. detailId는 절대 변경하지 마세요.
                3. visitDate는 변경하지 마세요.
                4. 같은 visitDate에 속한 관광지끼리만 방문 순서를 최적화하세요.
                5. 이동 거리가 최대한 짧아지도록 순서를 결정하세요.
                6. 각 날짜별 visitOrder는 1부터 시작하세요.
                7. 설명이나 마크다운을 포함하지 마세요.
                8. 반드시 아래 JSON 배열 형식으로만 응답하세요.

                응답 예시:
                [
                  {
                    "detailId": 10,
                    "visitOrder": "1"
                  },
                  {
                    "detailId": 12,
                    "visitOrder": "2"
                  }
                ]

                여행 일정:
                """);

        for (TripDetailVO detail : details) {

            prompt.append("\n");
            prompt.append("- detailId: ")
                    .append(detail.getDetailId());

            prompt.append(", title: ")
                    .append(detail.getTitle());

            prompt.append(", visitDate: ")
                    .append(detail.getVisitDate());

            prompt.append(", currentVisitOrder: ")
                    .append(detail.getVisitOrder());

            prompt.append(", mapX: ")
                    .append(detail.getMapx());

            prompt.append(", mapY: ")
                    .append(detail.getMapy());

            prompt.append(", address: ")
                    .append(detail.getAddr1());
        }

        return prompt.toString();
    }


    /**
     * Gemini가 반환한 JSON을 AiRouteOrderVO 목록으로 변환
     */
    private List<AiRouteOrderVO> parseRouteResponse(String response) {

        try {
            String json = response
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            JsonNode root = objectMapper.readTree(json);

            if (!root.isArray()) {
                throw new IllegalArgumentException(
                        "Gemini의 루트 최적화 응답 형식이 올바르지 않습니다."
                );
            }

            List<AiRouteOrderVO> result = new ArrayList<>();

            for (JsonNode item : root) {

                Integer detailId = item
                        .path("detailId")
                        .asInt();

                String visitOrder = item
                        .path("visitOrder")
                        .asText();

                if (detailId == 0
                        || visitOrder == null
                        || visitOrder.isBlank()) {

                    throw new IllegalArgumentException(
                            "Gemini 응답에 필요한 값이 없습니다."
                    );
                }

                result.add(
                        AiRouteOrderVO.builder()
                                .detailId(detailId)
                                .visitOrder(visitOrder)
                                .build()
                );
            }

            return result;

        } catch (Exception e) {
            log.error(
                    "Gemini 루트 최적화 응답 파싱 실패 - response: {}, error: {}",
                    response,
                    e.getMessage()
            );

            throw new RuntimeException(
                    "Gemini 루트 최적화 결과를 처리할 수 없습니다."
            );
        }
    }
}