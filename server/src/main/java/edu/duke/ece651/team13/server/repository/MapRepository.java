package edu.duke.ece651.team13.server.repository;

import edu.duke.ece651.team13.server.entity.MapEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the MapEntity
 */
@Repository
public interface MapRepository extends CrudRepository<MapEntity, Long> {
}
