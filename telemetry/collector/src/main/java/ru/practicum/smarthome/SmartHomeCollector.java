package ru.practicum.smarthome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartHomeCollector {

    public static void main(String[] args) {
        SpringApplication.run(SmartHomeCollector.class, args);
    }

}
