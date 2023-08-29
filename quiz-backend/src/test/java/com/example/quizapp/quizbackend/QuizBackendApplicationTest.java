package com.example.quizapp.quizbackend;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class QuizBackendApplicationTest {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		// Ensure that the application context loads successfully
		assertNotNull(applicationContext);
	}

	@Autowired
	private RestTemplate restTemplate;

	@Test
	void restTemplateBeanExists() {
		// Check if the restTemplate bean is created
		assertNotNull(restTemplate);
	}

	@Autowired
	private ModelMapper modelMapper;

	@Test
	void modelMapperBeanExists() {
		// Check if the modelMapper bean is created
		assertNotNull(modelMapper);
	}
}
