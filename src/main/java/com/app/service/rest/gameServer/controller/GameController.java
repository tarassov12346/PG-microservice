package com.app.service.rest.gameServer.controller;

import com.app.service.rest.gameServer.daoservice.DaoGameService;
import com.app.service.rest.gameServer.model.Game;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    private final DaoGameService daoGameService;
    // Конструктор для внедрения зависимости
    public GameController(DaoGameService daoGameService) {
        this.daoGameService = daoGameService;
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
