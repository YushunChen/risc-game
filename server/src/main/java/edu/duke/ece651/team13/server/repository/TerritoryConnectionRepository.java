package edu.duke.ece651.team13.server.repository;

import edu.duke.ece651.team13.server.entity.TerritoryConnectionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Repository for TerritoryConnectionEntity
 */
@Repository
public interface TerritoryConnectionRepository extends CrudRepository<TerritoryConnectionEntity, Long> {
}
