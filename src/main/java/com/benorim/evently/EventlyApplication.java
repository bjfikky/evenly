package com.benorim.evently;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EventlyApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventlyApplication.class, args);
    }

}
