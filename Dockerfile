# Этап 1: Сборка
FROM maven:3.8.6-jdk-8 AS builder
WORKDIR /app
COPY pom.xml .
# Сначала загружаем зависимости (кеширование)
RUN mvn dependency:go-offline
COPY src ./src
# Исправляем возможную опечатку в папке ресурсов, если она есть
RUN if [ -d "src/main/resourсes" ]; then mv src/main/resourсes src/main/resources; fi
RUN mvn clean package -DskipTests

# Этап 2: Запуск
FROM openjdk:8-jre-slim
WORKDIR /app
# Копируем JAR из предыдущего этапа
COPY --from=builder /app/target/PG-microservice-0.0.1-SNAPSHOT.jar app.jar

# Порт из вашего game-server.properties
EXPOSE 2222

# Запуск с указанием имени конфига
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.name=game-server"]
