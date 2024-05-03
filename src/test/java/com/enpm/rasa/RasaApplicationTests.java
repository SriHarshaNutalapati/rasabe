package com.enpm.rasa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RasaApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainMethodTest() {
		// Arrange
		String[] args = new String[]{};

		// Act & Assert
		RasaApplication.main(args);
	}

}
