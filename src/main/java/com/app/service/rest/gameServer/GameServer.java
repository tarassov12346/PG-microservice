package com.app.service.rest.gameServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan

public class GameServer {
    public static void main(String[] args) {
        System.setProperty("spring.config.name", "game-server");
        SpringApplication.run(GameServer.class, args);
    }
}
