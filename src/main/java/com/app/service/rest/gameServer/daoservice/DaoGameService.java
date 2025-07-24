package com.app.service.rest.gameServer.daoservice;

import com.app.service.rest.gameServer.model.Game;

import java.util.List;

public interface DaoGameService {
    void recordScore(Game game);
    void retrieveScores();
    void retrievePlayerScores(String playerName);
    List<Game> getAllGames();
    String getBestPlayer();
    int getBestScore();
    int getPlayerBestScore();
    int getPlayerAttemptsNumber();
    Long deleteByName(String name);
}
