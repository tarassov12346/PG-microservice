package com.app.service.rest.gameServer.daoservice;

import com.app.service.rest.gameServer.model.Game;

import java.util.List;

public interface DaoGameService {
    void recordScore(Game game);
    List<Game> getAllGames();
    String getBestPlayer();
    int getBestScore();
    int getPlayerBestScore(String playerName);
    int getPlayerAttemptsNumber(String playerName);
    Long deleteByName(String name);
}
