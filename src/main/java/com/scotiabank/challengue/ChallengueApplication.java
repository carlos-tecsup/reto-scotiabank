package com.scotiabank.challengue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.scotiabank.challengue",
})
public class ChallengueApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengueApplication.class, args);
	}

}
