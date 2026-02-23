package com.app.service.rest.gameServer.repository;

import com.app.service.rest.gameServer.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game,Long> {
    // Найдем все игры конкретного игрока прямо в БД
    List<Game> findAllByPlayerName(String playerName);
    Long deleteByPlayerName(String name);
}
