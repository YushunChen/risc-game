package edu.duke.ece651.team13.server.repository;

import edu.duke.ece651.team13.server.entity.UnitEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Repository for UnitEntity
 */
@Repository
public interface UnitRepository extends CrudRepository<UnitEntity, Long> {

}
