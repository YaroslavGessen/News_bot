package com.yaroslav.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.yaroslav")
public class NewsBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsBotApplication.class, args);
	}

}
