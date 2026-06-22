package com.app.service.rest.gameServer.configuration;

import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
public class GrpcLoomConfig {
    @Bean
    @ConditionalOnProperty(name = "spring.threads.virtual.enabled", havingValue = "true")
    public GrpcServerConfigurer grpcVirtualThreadsConfigurer() {
        // Заставляем gRPC сервер обрабатывать каждый запрос в новом виртуальном потоке
        return serverBuilder -> serverBuilder.executor(Executors.newVirtualThreadPerTaskExecutor());
    }
}
