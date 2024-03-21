package project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {
//http://localhost:8081/swagger-ui/index.html#/ pentru documentatie
	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
}
