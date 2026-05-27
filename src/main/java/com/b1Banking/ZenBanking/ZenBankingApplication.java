package com.b1Banking.ZenBanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZenBankingApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ZenBankingApplication.class, args);
	}

}
