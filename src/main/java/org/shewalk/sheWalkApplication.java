package org.shewalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class sheWalkApplication {
    public static void main(String[] args) {
        SpringApplication.run(sheWalkApplication.class, args);
    }
}
