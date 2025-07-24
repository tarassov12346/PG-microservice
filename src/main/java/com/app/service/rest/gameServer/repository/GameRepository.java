package com.app.service.rest.gameServer.repository;

import com.app.service.rest.gameServer.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game,Long> {
    Long deleteByPlayerName(String name);
}
