package com.enpm.rasa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath*:application-context.xml"})
public class RasaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RasaApplication.class, args);
	}

}
