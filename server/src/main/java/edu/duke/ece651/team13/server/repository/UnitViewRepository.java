package edu.duke.ece651.team13.server.repository;

import edu.duke.ece651.team13.server.entity.UnitViewEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitViewRepository extends CrudRepository<UnitViewEntity, Long> {

}
