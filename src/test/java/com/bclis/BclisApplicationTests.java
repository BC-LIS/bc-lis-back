package com.bclis;

import com.bclis.controller.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@ComponentScan(
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {
				AuthController.class,
				CategoryController.class,
				CommentController.class,
				ControllerAdvice.class,
				DocumentController.class,
				TypeController.class,
				UserController.class
		})
)
class BclisApplicationTests {

	@Test
	void contextLoads() {
		
	}
}
