package com.app.service.rest.gameServer.controller;

import com.app.service.rest.gameServer.daoservice.DaoGameService;
import com.app.service.rest.gameServer.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GameController {

    @Autowired
    private DaoGameService daoGameService;

    @RequestMapping("/score")
    public String doAdd(@RequestParam(defaultValue = "NoName") String playerName) {
        return "{\"bestplayer\":\"" + daoGameService.getBestPlayer() + "\"" + "," +
                "\"bestscore\":\"" + daoGameService.getBestScore() + "\"" + "," +
                "\"playerbestscore\":\"" + daoGameService.getPlayerBestScore(playerName) + "\"" + "," +
                "\"playerAttemptsNumber\":\"" + daoGameService.getPlayerAttemptsNumber(playerName) + "\"}";
    }

    @RequestMapping("/games")
    public List<Game> getAllGames() {
        daoGameService.getAllGames().forEach(game -> System.out.println(game.getPlayerName() + " " + game.getPlayerScore()));
        return daoGameService.getAllGames();
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
