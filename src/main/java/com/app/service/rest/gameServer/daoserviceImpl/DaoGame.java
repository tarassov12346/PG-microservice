package com.app.service.rest.gameServer.daoserviceImpl;

import com.app.service.rest.gameServer.daoservice.DaoGameService;
import com.app.service.rest.gameServer.model.Game;
import com.app.service.rest.gameServer.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class DaoGame implements DaoGameService {
    private String bestPlayer = "To be shown";
    private int bestScore;
    private int playerBestScore;
    private int playerAttemptsNumber;

    @Autowired
    private GameRepository gameRepository;

    @Override
    public void recordScore(Game game) {
        gameRepository.save(game);
    }

    @Override
    public void retrieveScores() {
        List<Game> playerList = gameRepository.findAll();
        Collections.sort(playerList, Comparator.comparingInt(Game::getPlayerScore));
        if (playerList.size() > 0) {
            bestPlayer = playerList.get(playerList.size() - 1).getPlayerName();
            bestScore = playerList.get(playerList.size() - 1).getPlayerScore();
        } else {
            bestPlayer = "To be seen yet!";
            bestScore = 0;
        }
    }

    @Override
    public void retrievePlayerScores(String playerName) {
        List<Game> playerByNameList = new ArrayList<>();
        List<Game> playerList = gameRepository.findAll();
        playerList.forEach(allPlayer -> {
            if (allPlayer.getPlayerName().equals(playerName)) playerByNameList.add(allPlayer);
        });
        Collections.sort(playerByNameList, Comparator.comparingInt(Game::getPlayerScore));
        if (playerByNameList.size() > 0)
            playerBestScore = playerByNameList.get(playerByNameList.size() - 1).getPlayerScore();
        else playerBestScore = 0;
        playerAttemptsNumber = playerByNameList.size();
    }

    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Override
    public String getBestPlayer() {
        return bestPlayer;
    }

    @Override
    public int getBestScore() {
        return bestScore;
    }

    @Override
    public int getPlayerBestScore() {
        return playerBestScore;
    }

    @Override
    public int getPlayerAttemptsNumber() {
        return playerAttemptsNumber;
    }

    @Override
    @Transactional
    public Long deleteByName(String name) {
        return gameRepository.deleteByPlayerName(name);
    }
}
