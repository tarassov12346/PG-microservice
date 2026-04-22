package com.app.service.rest.gameServer.controller;

import com.app.service.rest.gameServer.daoservice.DaoGameService;
import com.app.service.rest.gameServer.model.Game;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class GameController {
    private final DaoGameService daoGameService;

    // Конструктор для внедрения зависимости
    public GameController(DaoGameService daoGameService) {
        this.daoGameService = daoGameService;
    }

    @RequestMapping("/score")
    public CompletableFuture<Map<String, Object>> doAdd(@RequestParam(defaultValue = "NoName") String playerName) {
        // Запускаем все запросы параллельно в виртуальных потоках
        var bestPlayerFuture = CompletableFuture.supplyAsync(() -> daoGameService.getBestPlayer());
        var bestScoreFuture = CompletableFuture.supplyAsync(() -> daoGameService.getBestScore());
        var playerBestFuture = CompletableFuture.supplyAsync(() -> daoGameService.getPlayerBestScore(playerName));
        var playerAttemptsFuture = CompletableFuture.supplyAsync(() -> daoGameService.getPlayerAttemptsNumber(playerName));
        // Ждем завершения всех и собираем результат в JSON (Map)
        return CompletableFuture.allOf(bestPlayerFuture, bestScoreFuture, playerBestFuture, playerAttemptsFuture)
                .thenApply(v -> Map.of(
                        "bestplayer", bestPlayerFuture.join(),
                        "bestscore", bestScoreFuture.join(),
                        "playerbestscore", playerBestFuture.join(),
                        "playerAttemptsNumber", playerAttemptsFuture.join()
                ));
    }

    @RequestMapping("/games")
    public List<Game> getAllGames() {
        List<Game> games = daoGameService.getAllGames();
        return games;
    }

    @RequestMapping("/delete")
    public void doDelete(@RequestParam String playerName) {
        daoGameService.deleteByName(playerName);
    }

    @RequestMapping("/record")
    public void doRecord(@RequestBody Game game) {
        daoGameService.recordScore(game);
    }
}
