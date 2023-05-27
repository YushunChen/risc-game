package edu.duke.ece651.team13.server.repository;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for PlayerEntity
 */
@Repository
public interface PlayerRepository extends CrudRepository<PlayerEntity, Long>{

    /**
     * Finds a list of PlayerEntity objects based on the UserEntity associated with them.
     * @param user the UserEntity associated with the PlayerEntity objects to be found
     * @return a list of PlayerEntity objects associated with the given UserEntity
     */
    List<PlayerEntity> findByUser(UserEntity user);

    /**
     * Finds a PlayerEntity object based on the UserEntity and GameEntity associated with it.
     * @param user the UserEntity associated with the PlayerEntity object to be found
     * @param game the GameEntity associated with the PlayerEntity object to be found
     * @return an optional containing the PlayerEntity object associated with the given UserEntity and GameEntity
     */
    Optional<PlayerEntity> findByUserAndGame(UserEntity user, GameEntity game);
}
