package com.kraken.cube.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
    "com.kraken.cube.auth.entity",    // Пакет с вашими сущностями
    "com.kraken.cube.outboxmessage"   // Пакет с сущностями библиотеки
})
@EnableJpaRepositories(basePackages = {
    "com.kraken.cube.auth.repository", // Пакет с вашими репозиториями
    "com.kraken.cube.outboxmessage"    // Пакет с репозиториями библиотеки
})
public class AuthApplication {

	public static void main(String[] args) {
        System.out.println("--- DEBUG PATHS ---");
    System.out.println("Working Directory (user.dir): " + System.getProperty("user.dir"));
    
    // 2. Проверяем переменную окружения, если вы её настраивали в launch.json
    System.out.println("COMPOSE_PATH from Env: " + System.getenv("COMPOSE_PATH"));
    System.out.println("-------------------");
		SpringApplication.run(AuthApplication.class, args);
	}

}
