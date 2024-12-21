package de.propra.exambyte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("de.propra.exambyte.repository")
public class ExambyteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExambyteApplication.class, args);
	}

}
