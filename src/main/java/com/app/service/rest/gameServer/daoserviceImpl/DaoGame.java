package com.app.service.rest.gameServer.daoserviceImpl;

import com.app.service.rest.gameServer.daoservice.DaoGameService;
import com.app.service.rest.gameServer.model.Game;
import com.app.service.rest.gameServer.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;

@Service
public class DaoGame implements DaoGameService {

    @Autowired
    private GameRepository gameRepository;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "best_result", allEntries = true),
            // Добавляем суффиксы, чтобы Spring нашел именно те записи, которые мы создали
            @CacheEvict(value = "player_stats", key = "#game.playerName + '_best'"),
            @CacheEvict(value = "player_stats", key = "#game.playerName + '_attempts'"),
            @CacheEvict(value = "games", allEntries = true)
    })
    public void recordScore(Game game) {
        gameRepository.save(game);
    }

    @Override
    @Cacheable(value = "games", key = "'allGames'")
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Override
    @Cacheable(value = "player_stats", key = "#playerName + '_best'")
    public int getPlayerBestScore(String playerName) {
        // Логика прямо здесь, чтобы Spring перехватил вызов
        return gameRepository.findAllByPlayerName(playerName).stream()
                .mapToInt(Game::getPlayerScore)
                .max()
                .orElse(0);
    }

    @Override
    @Cacheable(value = "player_stats", key = "#playerName + '_attempts'")
    public int getPlayerAttemptsNumber(String playerName) {
        // И здесь тоже вызываем репозиторий напрямую
        return gameRepository.findAllByPlayerName(playerName).size();
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "games", allEntries = true),
            @CacheEvict(value = "player_stats", key = "#name + '_best'"),
            @CacheEvict(value = "player_stats", key = "#name + '_attempts'"),
            @CacheEvict(value = "best_result", allEntries = true)
    })
    public Long deleteByName(String name) {
        return gameRepository.deleteByPlayerName(name);
    }

    @Override
    @Cacheable(value = "best_result", key = "'player'")
    public String getBestPlayer() {
        // Вся логика должна быть здесь, а не в другом методе этого же класса
        return gameRepository.findAll().stream()
                .max(Comparator.comparingInt(Game::getPlayerScore))
                .map(Game::getPlayerName)
                .orElse("To be seen yet!");
    }

    @Override
    @Cacheable(value = "best_result", key = "'score'")
    public int getBestScore() {
        // Аналогично для счета
        return gameRepository.findAll().stream()
                .mapToInt(Game::getPlayerScore)
                .max()
                .orElse(0);
    }
}
