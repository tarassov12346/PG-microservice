package com.app.service.rest.gameServer.controller;

import com.app.service.rest.gameServer.daoservice.DaoGameService;
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

}
