package com.app.service.rest.gameServer.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String playerName;

    @Column(name = "score")
    private int playerScore;

    public Game() {
    }
}
