package com.app.service.rest.gameServer;

import com.app.service.grpc.*;
import com.app.service.rest.gameServer.daoservice.DaoGameService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@Slf4j
public class GameGrpcService extends GameIntegrationServiceGrpc.GameIntegrationServiceImplBase {
    private final DaoGameService daoGameService;

    public GameGrpcService(DaoGameService daoGameService) {
        this.daoGameService = daoGameService;
    }

    @Override
    public void getGameScore(PlayerRequest request, StreamObserver<GameScoreResponse> responseObserver) {
        log.info("🛰️ ПОЛУЧЕН gRPC ЗАПРОС ОТ ТЕТРИСА ДЛЯ: {}", request.getPlayerName());
        String name = request.getPlayerName();

        GameScoreResponse response = GameScoreResponse.newBuilder()
                .setBestPlayer(daoGameService.getBestPlayer())
                .setBestScore(daoGameService.getBestScore())
                .setPlayerBestScore(daoGameService.getPlayerBestScore(name))
                .setPlayerAttemptsNumber(daoGameService.getPlayerAttemptsNumber(name))
                .build();
        responseObserver.onNext(response); // Отправляем данные
        responseObserver.onCompleted();    // Закрываем поток
    }

    @Override
    public void getAllGames(Empty request, StreamObserver<AllGamesResponse> responseObserver) {
        log.info("🛰️ gRPC СЕРВЕР: Получен запрос на весь список игр");
        try {
            // 1. Получаем список из твоего DAO (убедись, что метод возвращает List<Game>)
            var allGamesFromDb = daoGameService.getAllGames();

            // 2. Маппим наши Entity/POJO в gRPC сообщения GameResponse
            List<GameResponse> protoGames = allGamesFromDb.stream()
                    .map(game -> GameResponse.newBuilder()
                            .setId(game.getId()) // Если id в БД - Long, gRPC int64 его примет
                            .setPlayerName(game.getPlayerName())
                            .setPlayerScore(game.getPlayerScore())
                            .build())
                    .toList();
            // 3. Собираем финальный ответ
            AllGamesResponse response = AllGamesResponse.newBuilder()
                    .addAllGames(protoGames) // ВАЖНО: заполняем repeated поле
                    .build();
            log.info("🛰️ gRPC СЕРВЕР: Отправляем {} записей", protoGames.size());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("❌ Ошибка на gRPC сервере: {}", e.getMessage());
            responseObserver.onError(e);
        }
    }

    @Override
    public void doRecord(GameRecordRequest request, StreamObserver<Empty> responseObserver) {
        log.info("📥 gRPC СЕРВЕР: Пишем рекорд для {}", request.getPlayerName());

        // 1. Создаем объект (исправляем конструктор)
        var game = new com.app.service.rest.gameServer.model.Game();
        game.setPlayerName(request.getPlayerName());
        game.setPlayerScore(request.getPlayerScore());

        // 2. Вызываем метод DAO
        daoGameService.recordScore(game);

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
