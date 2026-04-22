package com.app.service.rest.gameServer.repository;

import com.app.service.rest.gameServer.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game,Long> {

    Long deleteByPlayerName(String name);
    // Находим одну игру с максимальным счетом
    // SQL: SELECT * FROM games ORDER BY player_score DESC LIMIT 1
    Optional<Game> findFirstByOrderByPlayerScoreDesc();

    // Находим само число (максимальный счет)
    // SQL: SELECT MAX(player_score) FROM games
    @Query("SELECT MAX(g.playerScore) FROM Game g")
    Integer findMaxScore();

    // Для счета (агрегатная функция MAX)
    @Query("SELECT MAX(g.playerScore) FROM Game g WHERE g.playerName = :playerName")
    Integer findMaxScoreByPlayerName(@Param("playerName") String playerName);

    @Query(value = "SELECT DISTINCT ON (name) id, name, score " +
            "FROM games " +
            "ORDER BY name, score DESC",
            nativeQuery = true)
    List<Game> findAllBestResultsNative();
    // Для попыток (агрегатная функция COUNT)
    // Spring сам поймет по названию, что нужен SELECT COUNT(*)
    long countByPlayerName(String playerName);
}
