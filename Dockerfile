# ==============================================================================
# Этап 1: Сборка gRPC и JAR-артефакта (Среда Ubuntu для совместимости с protoc)
# ==============================================================================
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Копируем дескриптор сборки для кэширования зависимостей Maven
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем исходный код приложения
COPY src ./src

# СОХРАНЯЕМ КОСТЫЛЬ: Исправляем возможную опечатку с русской буквой "с" в названии папки ресурсов
RUN if [ -d "src/main/resourсes" ]; then mv src/main/resourсes src/main/resources; fi

# Генерируем gRPC-классы, подменяем javax на jakarta через Antrun и собираем проект
RUN mvn clean package -DskipTests

# ==============================================================================
# Этап 2: Высокопроизводительный запуск (Ultra-Low Latency Runtime)
# ==============================================================================
# Используем легковесный JRE 21 на базе Alpine Linux
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Создаем безопасного не-root пользователя
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Копируем собранный JAR-файл из этапа сборки
COPY --from=builder /app/target/PG-microservice-0.0.1-SNAPSHOT.jar app.jar

# ОТКРЫВАЕМ ПОРТЫ ИЗ PROPERTIES:
# 2222 - HTTP сервер / Эврика
# 6566 - gRPC сервер базы данных
EXPOSE 2222 6566

# Точка входа с поддержкой контейнеризации, ZGC и явным указанием твоего имени конфига
ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-XX:+UseZGC", \
            "-jar", "app.jar", \
            "--spring.config.name=game-server"]
