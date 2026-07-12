package com.trip.jeju.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.utils.SpringDocUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
public class SwaggerConfig {
    static {
        // 💡 스웨거가 자바8 날짜 타입을 만났을 때 String(문자열) 창으로 입력받도록 강제하는 설정입니다.
        // 이 설정이 없으면 내부 스캔 중 500 에러가 나거나 Request Body가 먹통이 됩니다.
        SpringDocUtils.getConfig()
                .replaceWithClass(LocalDateTime.class, String.class)
                .replaceWithClass(LocalDate.class, String.class)
                .replaceWithClass(LocalTime.class, String.class);
    }

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme apiKey = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .scheme("bearer")
                .bearerFormat("JWT");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Token");

        return new OpenAPI()
                .info(new Info()
                        .title("API 명세서")
                        .description("프로젝트 API 문서 및 테스트 화면")
                        .version("v1.0.0"))
                .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
                .addSecurityItem(securityRequirement);
    }
}
