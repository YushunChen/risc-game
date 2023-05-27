package edu.duke.ece651.team13.server.repository;

import edu.duke.ece651.team13.server.entity.TerritoryViewEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerritoryViewRepository extends CrudRepository<TerritoryViewEntity, Long> {

}
