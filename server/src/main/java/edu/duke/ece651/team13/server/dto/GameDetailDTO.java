package edu.duke.ece651.team13.server.dto;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is the DTO sent when /getGameForUser
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDetailDTO {
    private GameEntity game;
    private PlayerEntity player;
    private boolean isPlayerDone;
}
