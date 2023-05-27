package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.MapEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryConnectionEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;
import edu.duke.ece651.team13.server.repository.TerritoryConnectionRepository;
import edu.duke.ece651.team13.server.repository.TerritoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TerritoryServiceImpl implements TerritoryService {

    @Autowired
    private final TerritoryRepository repository;

    @Autowired
    private final TerritoryConnectionRepository territoryConnectionRepository;

    @Override
    @Transactional
    public TerritoryEntity createTerritory(String name, MapEntity map, PlayerEntity player, int foodProduction, int techProduction) {
        TerritoryEntity territory = new TerritoryEntity();
        territory.setMap(map);
        map.getTerritories().add(territory);
        territory.setName(name);
        territory.setOwner(player);
        territory.setFoodProduction(foodProduction);
        territory.setTechProduction(techProduction);
        return repository.save(territory);
    }


    @Override
    public TerritoryEntity getTerritoriesById(Long Id) {
        Optional<TerritoryEntity> territory = repository.findById(Id);
        if (territory.isPresent()) {
            return territory.get();
        } else {
            log.error("Did not find Territory with Id " + Id);
            throw new NoSuchElementException("Territory with Id " + Id + " does not exists");
        }
    }

    @Override
    public List<TerritoryEntity> getTerritoriesByPlayer(PlayerEntity player) {
        return repository.findByOwner(player);
    }

    @Override
    @Transactional
    public TerritoryEntity updateTerritoryOwner(TerritoryEntity territory, PlayerEntity owner) {
        territory.setOwner(owner);
        return repository.save(territory);
    }

    @Override
    @Transactional
    public void addNeighbour(TerritoryEntity territory1, TerritoryEntity territory2, Integer distance) {
        TerritoryConnectionEntity connection1 = territoryConnectionRepository.save(new TerritoryConnectionEntity(territory1, territory2, distance));
        territory1.getConnections().add(connection1);
        TerritoryConnectionEntity connection2 = territoryConnectionRepository.save(new TerritoryConnectionEntity(territory2, territory1, distance));
        territory2.getConnections().add(connection2);
    }

    @Override
    @Transactional
    public TerritoryEntity updateTerritoryUnits(TerritoryEntity territory, List<UnitEntity> units) {
        log.info("Updating territory " + territory.getId() + " units.");
        territory.setUnits(units);
        return repository.save(territory);
    }

    /**
     * update territory's remaining number of turns for which the territory should be cloaked
     * two cases:
     * 1. for territory which is cloaked, decrease "remainCloak" by 1 each round
     * 2. for territory need to be cloaked, set "remainCloak"
     * @param territory to be updated
     * @return territory with up-to-date RemainingCloak
     */
    @Override
    @Transactional
    public TerritoryEntity updateTerritoryRemainingCloak(TerritoryEntity territory, int remainingCloak){
        territory.setRemainingCloak(remainingCloak);
        return repository.save(territory);
    }
}
