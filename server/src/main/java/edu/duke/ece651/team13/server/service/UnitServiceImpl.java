package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import edu.duke.ece651.team13.server.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

    @Autowired
    private final UnitRepository repository;

    @Override
    @Transactional
    public UnitEntity createUnit(UnitMappingEnum unitType, TerritoryEntity territory, int unitNum) {
        UnitEntity unit = new UnitEntity();
        unit.setTerritory(territory);
        territory.getUnits().add(unit);
        unit.setUnitType(unitType);
        unit.setUnitNum(unitNum);
        return repository.save(unit);
    }

    @Override
    @Transactional
    public UnitEntity updateUnit(UnitEntity unit, int unitNum){
        unit.setUnitNum(unitNum);
        return repository.save(unit);
    }

}
