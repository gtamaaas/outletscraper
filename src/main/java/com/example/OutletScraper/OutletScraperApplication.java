package com.example.OutletScraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OutletScraperApplication {

	public static void main(String[] args) {
		SpringApplication.run(OutletScraperApplication.class, args);
	}

}
