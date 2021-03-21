package com.maktabatic.msretard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsRetardApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsRetardApplication.class, args);
	}

}
