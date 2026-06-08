package com.trip.jeju;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.trip.jeju.*.mapper")
public class JejuApplication {

	public static void main(String[] args) {
		SpringApplication.run(JejuApplication.class, args);
	}

}
