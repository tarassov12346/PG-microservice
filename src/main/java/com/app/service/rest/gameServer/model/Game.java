package com.app.service.rest.gameServer.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "games_seq_gen")
    @SequenceGenerator(
            name = "games_seq_gen",
            sequenceName = "games_sequence", // Имя сиквенса в базе данных
            allocationSize = 50              // Будем выделять по 50 ID за один запрос к СУБД
    )
    @Column(name = "id")
    private Long id;


    @Column(name = "name")
    private String playerName;

    @Column(name = "score")
    private int playerScore;

    public Game() {
    }
}
