package edu.duke.ece651.team13.server.repository;

import edu.duke.ece651.team13.server.entity.SpyUnitEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Repository for SpyUnitEntity
 */
@Repository
public interface SpyUnitRepository extends CrudRepository<SpyUnitEntity, Long>  {
}
