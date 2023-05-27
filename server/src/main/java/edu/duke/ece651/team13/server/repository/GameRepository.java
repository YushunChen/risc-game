package edu.duke.ece651.team13.server.repository;

import edu.duke.ece651.team13.server.entity.GameEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repository for the GameEntity class
 */
@Repository
public interface GameRepository extends CrudRepository<GameEntity, Long> {

    /**
     * Finds a list of GameEntity objects by the round number.
     * @param roundNo the round number to search for
     * @return a list of GameEntity objects matching the given round number
     */
    List<GameEntity> findByRoundNo(int roundNo);
}
