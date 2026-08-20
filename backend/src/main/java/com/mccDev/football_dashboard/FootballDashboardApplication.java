package com.mccDev.football_dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FootballDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballDashboardApplication.class, args);
	}

}
