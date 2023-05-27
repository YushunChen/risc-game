package edu.duke.ece651.team13.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is the DTO contained in GamesDTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private Long Id;
    private int noOfPlayer;
}
