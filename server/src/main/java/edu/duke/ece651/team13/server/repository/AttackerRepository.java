package edu.duke.ece651.team13.server.repository;

import edu.duke.ece651.team13.server.entity.AttackerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repository for AttackerEntity objects.
 */
@Repository
public interface AttackerRepository extends CrudRepository<AttackerEntity, Long> {

    /**
     * Returns a list of AttackerEntities associated with the given TerritoryEntity.
     * @param territory the TerritoryEntity to find associated AttackerEntities for.
     * @return a list of AttackerEntities associated with the given TerritoryEntity.
     */
    List<AttackerEntity> findByTerritory(TerritoryEntity territory);

    /**
     * Deletes all AttackerEntities associated with the given TerritoryEntity.
     * @param territory the TerritoryEntity to delete associated AttackerEntities for.
     */
    void deleteByTerritory(TerritoryEntity territory);
}
