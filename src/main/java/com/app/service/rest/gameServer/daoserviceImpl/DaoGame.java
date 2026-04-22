package com.app.service.rest.gameServer.daoserviceImpl;

import com.app.service.rest.gameServer.daoservice.DaoGameService;
import com.app.service.rest.gameServer.model.Game;
import com.app.service.rest.gameServer.repository.GameRepository;
import jakarta.transaction.Transactional; // <-- ПРАВИЛЬНЫЙ ИМПОРТ
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j // Добавляет переменную log
@Service
public class DaoGame implements DaoGameService {

    private final GameRepository gameRepository;

    // Spring Boot автоматически подставит репозиторий, @Autowired здесь не обязателен
    public DaoGame(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    @Transactional // Добавь сюда
    @Caching(evict = {
            @CacheEvict(value = "best_result", allEntries = true),
            // Добавляем суффиксы, чтобы Spring нашел именно те записи, которые мы создали
            @CacheEvict(value = "player_stats", key = "#game.playerName + '_best'"),
            @CacheEvict(value = "player_stats", key = "#game.playerName + '_attempts'"),
            @CacheEvict(value = "games", allEntries = true)
    })
    public void recordScore(Game game) {
        // Лог в самом начале метода
        log.info("===> Вход в метод recordScore для игрока: {}", game.getPlayerName());
        gameRepository.save(game);
        log.info("<=== Метод recordScore завершен (save выполнен)");
    }

    @Override
    @Cacheable(value = "games", key = "'topRecords'")
    public List<Game> getAllGames() {
        // Вызываем наш кастомный Query
        return gameRepository.findAllBestResultsNative();
    }

    @Override
    @Cacheable(value = "player_stats", key = "#playerName + '_best'")
    public int getPlayerBestScore(String playerName) {
        // Вместо стрима — один SQL запрос, возвращающий только число
        Integer maxScore = gameRepository.findMaxScoreByPlayerName(playerName);
        return maxScore != null ? maxScore : 0;
    }

    @Override
    @Cacheable(value = "player_stats", key = "#playerName + '_attempts'")
    public int getPlayerAttemptsNumber(String playerName) {
        // Вместо загрузки всех сущностей и вызова .size() — легкий COUNT в базе
        return (int) gameRepository.countByPlayerName(playerName);
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
        // База сама сортирует и отдает только ОДНУ нужную запись
        return gameRepository.findFirstByOrderByPlayerScoreDesc()
                .map(Game::getPlayerName)
                .orElse("To be seen yet!");
    }

    @Override
    @Cacheable(value = "best_result", key = "'score'")
    public int getBestScore() {
        // База сама вычисляет число и возвращает 4 байта вместо всей таблицы
        Integer maxScore = gameRepository.findMaxScore();
        return maxScore != null ? maxScore : 0;
    }
}
