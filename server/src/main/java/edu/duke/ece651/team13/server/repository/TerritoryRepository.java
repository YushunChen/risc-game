package edu.duke.ece651.team13.server.repository;

import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data Repository for TerritoryEntity
 */
@Repository
public interface TerritoryRepository extends CrudRepository<TerritoryEntity, Long> {

    /**
     * Returns a list of TerritoryEntity objects owned by a given player.
     * @param owner the PlayerEntity object representing the owner whose territories are being searched for
     * @return a list of TerritoryEntity objects owned by the given player
     */
    List<TerritoryEntity> findByOwner(PlayerEntity owner);
}
