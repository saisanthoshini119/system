package com.example.system;

import com.example.system.config.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SystemApplication {

	public static void main(String[] args) {
		// Load .env (from current dir or system/.env) so HUGGINGFACE_API_KEY and MODEL are set
		EnvLoader.loadEnv();
		SpringApplication.run(SystemApplication.class, args);
	}

}
