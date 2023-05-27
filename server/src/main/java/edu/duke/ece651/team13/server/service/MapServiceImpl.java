package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.MapEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.SpyUnitEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.TerritoryViewEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import edu.duke.ece651.team13.server.repository.MapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MapServiceImpl implements MapService {

    @Autowired
    private final MapRepository repository;

    @Autowired
    private final TerritoryService territoryService;

    @Autowired
    private final UnitService unitService;

    @Autowired
    private final TerritoryViewService territoryViewService;

    @Autowired
    private final UnitViewService unitViewService;

    @Autowired
    private final SpyUnitService spyUnitService;

    @Override
    public MapEntity getMap(Long mapId) {
        Optional<MapEntity> map = repository.findById(mapId);
        if (map.isPresent()) {
            return map.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    @Transactional
    public MapEntity createMap(GameEntity gameEntity, List<PlayerEntity> players) {

        MapEntity mapEntity = new MapEntity();
        mapEntity.setGame(gameEntity);
        gameEntity.setMap(mapEntity);

        switch (players.size()) {
            case 2:
                createMapFor2players(mapEntity, players, 10);
                break;
            case 3:
                createMapFor3players(mapEntity, players, 10);
                break;
            default:
                createMapFor4players(mapEntity, players, 10);
        }

        return repository.save(mapEntity);
    }

    private void createMapFor4players(MapEntity mapEntity, List<PlayerEntity> players, int initialUnitNum) {
        TerritoryEntity rottweiler = territoryService.createTerritory("Rottweiler", mapEntity, players.get(0), 100, 100);
        TerritoryEntity dachshund = territoryService.createTerritory("Dachshund", mapEntity, players.get(0), 150, 150);
        TerritoryEntity beagle = territoryService.createTerritory("Beagle", mapEntity, players.get(0), 150, 150);
        TerritoryEntity labrador = territoryService.createTerritory("Labrador", mapEntity, players.get(0), 50, 50);
        TerritoryEntity poodle = territoryService.createTerritory("Poodle", mapEntity, players.get(0), 100, 100);
        TerritoryEntity bulldog = territoryService.createTerritory("Bulldog", mapEntity, players.get(0), 100, 100);

        TerritoryEntity boxer = territoryService.createTerritory("Boxer", mapEntity, players.get(1), 50, 50);
        TerritoryEntity havanese = territoryService.createTerritory("Havanese", mapEntity, players.get(1), 100, 100);
        TerritoryEntity spaniel = territoryService.createTerritory("Spaniel", mapEntity, players.get(1), 100, 100);
        TerritoryEntity sheepdog = territoryService.createTerritory("Sheepdog", mapEntity, players.get(1), 150, 150);
        TerritoryEntity akita = territoryService.createTerritory("Akita", mapEntity, players.get(1), 100, 100);
        TerritoryEntity brittany = territoryService.createTerritory("Brittany", mapEntity, players.get(1), 150, 150);

        TerritoryEntity vizsla = territoryService.createTerritory("Vizsla", mapEntity, players.get(2), 100, 100);
        TerritoryEntity pug = territoryService.createTerritory("Pug", mapEntity, players.get(2), 150, 150);
        TerritoryEntity chihuahua = territoryService.createTerritory("Chihuahua", mapEntity, players.get(2), 100, 100);
        TerritoryEntity maltese = territoryService.createTerritory("Maltese", mapEntity, players.get(2), 150, 150);
        TerritoryEntity mastiff = territoryService.createTerritory("Mastiff", mapEntity, players.get(2), 100, 100);
        TerritoryEntity collie = territoryService.createTerritory("Collie", mapEntity, players.get(2), 50, 50);

        TerritoryEntity dalmatian = territoryService.createTerritory("Dalmatian", mapEntity, players.get(3), 50, 50);
        TerritoryEntity papillon = territoryService.createTerritory("Papillon", mapEntity, players.get(3), 100, 100);
        TerritoryEntity setter = territoryService.createTerritory("Setter", mapEntity, players.get(3), 100, 100);
        TerritoryEntity samoyed = territoryService.createTerritory("Samoyed", mapEntity, players.get(3), 100, 100);
        TerritoryEntity bullmastiff = territoryService.createTerritory("Bullmastiff", mapEntity, players.get(3), 150, 150);
        TerritoryEntity whippet = territoryService.createTerritory("Whippet", mapEntity, players.get(3), 150, 150);

        territoryService.addNeighbour(dachshund, beagle, 3);
        territoryService.addNeighbour(dachshund, rottweiler, 5);
        territoryService.addNeighbour(dachshund, bulldog, 5);
        territoryService.addNeighbour(beagle, bulldog, 5);
        territoryService.addNeighbour(beagle, poodle, 5);
        territoryService.addNeighbour(rottweiler, bulldog, 3);
        territoryService.addNeighbour(bulldog, poodle, 3);
        territoryService.addNeighbour(rottweiler, labrador, 5);
        territoryService.addNeighbour(bulldog, labrador, 5);
        territoryService.addNeighbour(poodle, labrador, 5);

        territoryService.addNeighbour(brittany, sheepdog, 3);
        territoryService.addNeighbour(brittany, akita, 5);
        territoryService.addNeighbour(brittany, havanese, 5);
        territoryService.addNeighbour(sheepdog, havanese, 5);
        territoryService.addNeighbour(sheepdog, spaniel, 5);
        territoryService.addNeighbour(akita, havanese, 3);
        territoryService.addNeighbour(havanese, spaniel, 3);
        territoryService.addNeighbour(akita, boxer, 5);
        territoryService.addNeighbour(havanese, boxer, 5);
        territoryService.addNeighbour(spaniel, boxer, 5);

        territoryService.addNeighbour(pug, maltese, 3);
        territoryService.addNeighbour(pug, mastiff, 5);
        territoryService.addNeighbour(pug, vizsla, 5);
        territoryService.addNeighbour(maltese, vizsla, 5);
        territoryService.addNeighbour(maltese, chihuahua, 5);
        territoryService.addNeighbour(mastiff, vizsla, 3);
        territoryService.addNeighbour(vizsla, chihuahua, 3);
        territoryService.addNeighbour(mastiff, collie, 5);
        territoryService.addNeighbour(vizsla, collie, 5);
        territoryService.addNeighbour(chihuahua, collie, 5);

        territoryService.addNeighbour(bullmastiff, whippet, 3);
        territoryService.addNeighbour(bullmastiff, papillon, 5);
        territoryService.addNeighbour(bullmastiff, setter, 5);
        territoryService.addNeighbour(whippet, setter, 5);
        territoryService.addNeighbour(whippet, samoyed, 5);
        territoryService.addNeighbour(papillon, setter, 3);
        territoryService.addNeighbour(setter, samoyed, 3);
        territoryService.addNeighbour(papillon, dalmatian, 5);
        territoryService.addNeighbour(setter, dalmatian, 5);
        territoryService.addNeighbour(samoyed, dalmatian, 5);

        territoryService.addNeighbour(rottweiler, akita, 7);
        territoryService.addNeighbour(rottweiler, boxer, 7);
        territoryService.addNeighbour(poodle, mastiff, 7);
        territoryService.addNeighbour(labrador, mastiff, 7);

        territoryService.addNeighbour(spaniel, dalmatian, 7);
        territoryService.addNeighbour(spaniel, papillon, 7);

        territoryService.addNeighbour(samoyed, collie, 7);
        territoryService.addNeighbour(samoyed, chihuahua, 7);

        territoryService.addNeighbour(boxer, labrador, 7);
        territoryService.addNeighbour(labrador, collie, 7);
        territoryService.addNeighbour(collie, dalmatian, 7);
        territoryService.addNeighbour(dalmatian, boxer, 7);
        territoryService.addNeighbour(labrador, dalmatian, 7);
        territoryService.addNeighbour(boxer, collie, 7);

        initializeMap(mapEntity, players, initialUnitNum);
    }

    private void createMapFor3players(MapEntity mapEntity, List<PlayerEntity> players, int initialUnitNum) {
        TerritoryEntity rottweiler = territoryService.createTerritory("Rottweiler", mapEntity, players.get(0), 100, 100);
        TerritoryEntity dachshund = territoryService.createTerritory("Dachshund", mapEntity, players.get(0), 100, 100);
        TerritoryEntity beagle = territoryService.createTerritory("Beagle", mapEntity, players.get(0), 100, 100);
        TerritoryEntity labrador = territoryService.createTerritory("Labrador", mapEntity, players.get(0), 150, 150);
        TerritoryEntity poodle = territoryService.createTerritory("Poodle", mapEntity, players.get(0), 50, 50);
        TerritoryEntity bulldog = territoryService.createTerritory("Bulldog", mapEntity, players.get(0), 150, 150);

        TerritoryEntity boxer = territoryService.createTerritory("Boxer", mapEntity, players.get(1), 150, 150);
        TerritoryEntity havanese = territoryService.createTerritory("Havanese", mapEntity, players.get(1), 50, 50);
        TerritoryEntity spaniel = territoryService.createTerritory("Spaniel", mapEntity, players.get(1), 100, 100);
        TerritoryEntity sheepdog = territoryService.createTerritory("Sheepdog", mapEntity, players.get(1), 100, 100);
        TerritoryEntity akita = territoryService.createTerritory("Akita", mapEntity, players.get(1), 150, 150);
        TerritoryEntity brittany = territoryService.createTerritory("Brittany", mapEntity, players.get(1), 100, 100);

        TerritoryEntity vizsla = territoryService.createTerritory("Vizsla", mapEntity, players.get(2), 50, 50);
        TerritoryEntity pug = territoryService.createTerritory("Pug", mapEntity, players.get(2), 100, 100);
        TerritoryEntity chihuahua = territoryService.createTerritory("Chihuahua", mapEntity, players.get(2), 100, 100);
        TerritoryEntity maltese = territoryService.createTerritory("Maltese", mapEntity, players.get(2), 100, 100);
        TerritoryEntity mastiff = territoryService.createTerritory("Mastiff", mapEntity, players.get(2), 150, 150);
        TerritoryEntity collie = territoryService.createTerritory("Collie", mapEntity, players.get(2), 150, 150);

        territoryService.addNeighbour(labrador, bulldog, 3);
        territoryService.addNeighbour(labrador, rottweiler, 5);
        territoryService.addNeighbour(labrador, dachshund, 5);
        territoryService.addNeighbour(dachshund, bulldog, 5);
        territoryService.addNeighbour(bulldog, beagle, 5);
        territoryService.addNeighbour(rottweiler, dachshund, 3);
        territoryService.addNeighbour(dachshund, beagle, 3);
        territoryService.addNeighbour(rottweiler, poodle, 5);
        territoryService.addNeighbour(dachshund, poodle, 5);
        territoryService.addNeighbour(beagle, poodle, 5);
        territoryService.addNeighbour(spaniel, havanese, 5);
        territoryService.addNeighbour(brittany, havanese, 5);
        territoryService.addNeighbour(sheepdog, havanese, 5);
        territoryService.addNeighbour(spaniel, brittany, 3);
        territoryService.addNeighbour(brittany, sheepdog, 3);
        territoryService.addNeighbour(spaniel, boxer, 5);
        territoryService.addNeighbour(brittany, boxer, 5);
        territoryService.addNeighbour(brittany, akita, 5);
        territoryService.addNeighbour(sheepdog, akita, 5);
        territoryService.addNeighbour(boxer, akita, 3);
        territoryService.addNeighbour(vizsla, pug, 5);
        territoryService.addNeighbour(vizsla, chihuahua, 5);
        territoryService.addNeighbour(vizsla, maltese, 5);
        territoryService.addNeighbour(pug, chihuahua, 3);
        territoryService.addNeighbour(chihuahua, maltese, 3);
        territoryService.addNeighbour(mastiff, collie, 3);
        territoryService.addNeighbour(pug, mastiff, 5);
        territoryService.addNeighbour(chihuahua, mastiff, 5);
        territoryService.addNeighbour(chihuahua, collie, 5);
        territoryService.addNeighbour(maltese, collie, 5);

        territoryService.addNeighbour(rottweiler, spaniel, 7);
        territoryService.addNeighbour(rottweiler, havanese, 7);
        territoryService.addNeighbour(beagle, vizsla, 7);
        territoryService.addNeighbour(beagle, pug, 7);
        territoryService.addNeighbour(poodle, spaniel, 7);
        territoryService.addNeighbour(poodle, havanese, 7);
        territoryService.addNeighbour(poodle, vizsla, 7);
        territoryService.addNeighbour(poodle, pug, 7);
        territoryService.addNeighbour(havanese, vizsla, 7);
        territoryService.addNeighbour(havanese, maltese, 7);
        territoryService.addNeighbour(vizsla, sheepdog, 7);
        territoryService.addNeighbour(sheepdog, maltese, 7);

        initializeMap(mapEntity, players, initialUnitNum);
    }

    private void createMapFor2players(MapEntity mapEntity, List<PlayerEntity> players, int initialUnitNum) {
        TerritoryEntity rottweiler = territoryService.createTerritory("Rottweiler", mapEntity, players.get(0), 100, 100);
        TerritoryEntity dachshund = territoryService.createTerritory("Dachshund", mapEntity, players.get(0), 100, 100);
        TerritoryEntity beagle = territoryService.createTerritory("Beagle", mapEntity, players.get(0), 100, 100);
        TerritoryEntity labrador = territoryService.createTerritory("Labrador", mapEntity, players.get(0), 50, 50);
        TerritoryEntity poodle = territoryService.createTerritory("Poodle", mapEntity, players.get(0), 50, 50);
        TerritoryEntity bulldog = territoryService.createTerritory("Bulldog", mapEntity, players.get(0), 50, 50);

        TerritoryEntity boxer = territoryService.createTerritory("Boxer", mapEntity, players.get(1), 50, 50);
        TerritoryEntity havanese = territoryService.createTerritory("Havanese", mapEntity, players.get(1), 50, 50);
        TerritoryEntity spaniel = territoryService.createTerritory("Spaniel", mapEntity, players.get(1), 50, 50);
        TerritoryEntity sheepdog = territoryService.createTerritory("Sheepdog", mapEntity, players.get(1), 100, 100);
        TerritoryEntity akita = territoryService.createTerritory("Akita", mapEntity, players.get(1), 100, 100);
        TerritoryEntity brittany = territoryService.createTerritory("Brittany", mapEntity, players.get(1), 100, 100);

        territoryService.addNeighbour(rottweiler, dachshund, 3);
        territoryService.addNeighbour(dachshund, beagle, 3);
        territoryService.addNeighbour(rottweiler, labrador, 5);
        territoryService.addNeighbour(rottweiler, poodle, 5);
        territoryService.addNeighbour(dachshund, poodle, 5);
        territoryService.addNeighbour(dachshund, bulldog, 5);
        territoryService.addNeighbour(beagle, bulldog, 5);
        territoryService.addNeighbour(labrador, poodle, 3);
        territoryService.addNeighbour(poodle, bulldog, 3);

        territoryService.addNeighbour(boxer, havanese, 3);
        territoryService.addNeighbour(havanese, spaniel, 3);
        territoryService.addNeighbour(boxer, sheepdog, 5);
        territoryService.addNeighbour(sheepdog, havanese, 5);
        territoryService.addNeighbour(havanese, akita, 5);
        territoryService.addNeighbour(akita, spaniel, 5);
        territoryService.addNeighbour(spaniel, brittany, 5);
        territoryService.addNeighbour(sheepdog, akita, 3);
        territoryService.addNeighbour(akita, brittany, 3);

        territoryService.addNeighbour(labrador, boxer, 7);
        territoryService.addNeighbour(labrador, havanese, 7);
        territoryService.addNeighbour(poodle, havanese, 7);
        territoryService.addNeighbour(poodle, spaniel, 7);
        territoryService.addNeighbour(bulldog, spaniel, 7);

        initializeMap(mapEntity, players, initialUnitNum);
    }

    private void initializeMap(MapEntity mapEntity, List<PlayerEntity> players, int initialUnitNum) {
        initUnitForMap(mapEntity, initialUnitNum);
        initResourceForPlayers(players);
        initSpyUnitForMap(mapEntity, players);
        initTerritoryViewForTerritories(mapEntity, players);
    }

    /**
     * The initial units are "basic" units
     *
     * @param mapEntity      map to be initialized
     * @param initialUnitNum initial unit number of each territory
     */
    private void initUnitForMap(MapEntity mapEntity, int initialUnitNum) {
        for (TerritoryEntity territory : mapEntity.getTerritories()) {
            unitService.createUnit(UnitMappingEnum.LEVEL0, territory, initialUnitNum);
            unitService.createUnit(UnitMappingEnum.LEVEL1, territory, 0);
            unitService.createUnit(UnitMappingEnum.LEVEL2, territory, 0);
            unitService.createUnit(UnitMappingEnum.LEVEL3, territory, 0);
            unitService.createUnit(UnitMappingEnum.LEVEL4, territory, 0);
            unitService.createUnit(UnitMappingEnum.LEVEL5, territory, 0);
            unitService.createUnit(UnitMappingEnum.LEVEL6, territory, 0);
        }
    }

    private void initResourceForPlayers(List<PlayerEntity> players) {
        for (PlayerEntity playerEntity : players) {
            List<TerritoryEntity> territoryEntities = territoryService.getTerritoriesByPlayer(playerEntity);
            int foodResource = 0;
            int techResource = 0;
            for (TerritoryEntity territoryEntity : territoryEntities) {
                foodResource += territoryEntity.getFoodProduction();
                techResource += territoryEntity.getTechProduction();
            }
            playerEntity.setFoodResource(foodResource);
            playerEntity.setTechResource(techResource);
        }
    }

    /**
     * Each territory has (playersNum) territoryViews for different players respectively
     */
    private void initTerritoryViewForTerritories(MapEntity map, List<PlayerEntity> players){
        for(TerritoryEntity territoryToDisplay: map.getTerritories()){
            for (PlayerEntity viewer : players) {
                TerritoryViewEntity territoryView = territoryViewService.initTerritoryView(territoryToDisplay, viewer);
                for (UnitEntity unitToDisplay : territoryToDisplay.getUnits()) {
                    unitViewService.initUnitView(territoryView, unitToDisplay);
                }
            }
        }
    }

    /**
     * Each territory has (playersNum) SpyUnitEntities for different players respectively
     */
    private void initSpyUnitForMap(MapEntity map, List<PlayerEntity> players){
        for(TerritoryEntity territory: map.getTerritories()){
            for (PlayerEntity player : players) {
                SpyUnitEntity spyUnit = spyUnitService.createSpyUnit(territory, 0, player);
                territory.getSpyUnits().add(spyUnit);
            }
        }
    }
}
