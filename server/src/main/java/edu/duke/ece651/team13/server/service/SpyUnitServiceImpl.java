package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.SpyUnitEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.repository.SpyUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SpyUnitServiceImpl implements SpyUnitService {
    @Autowired
    private final SpyUnitRepository repository;

    @Override
    @Transactional
    public SpyUnitEntity createSpyUnit(TerritoryEntity territory, int unitNum, PlayerEntity player) {
        SpyUnitEntity spy = new SpyUnitEntity();
        spy.setTerritory(territory);
        territory.getSpyUnits().add(spy);
        spy.setUnitNum(unitNum);
        spy.setOwner(player);
        return repository.save(spy);
    }

    @Override
    @Transactional
    public SpyUnitEntity updateSpyUnit(SpyUnitEntity spy, int unitNum){
        spy.setUnitNum(unitNum);
        return repository.save(spy);
    }

}
