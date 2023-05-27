package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.AttackerEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import edu.duke.ece651.team13.server.repository.AttackerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttackerServiceImpl implements AttackerService {

    @Autowired
    private final AttackerRepository repository;

    @Override
    public List<AttackerEntity> getAttackers(TerritoryEntity territory) {
        return repository.findByTerritory(territory);
    }

    @Override
    @Transactional
    public AttackerEntity addAttacker(TerritoryEntity territory, PlayerEntity player, UnitMappingEnum unitType, Integer UnitNo) {
        AttackerEntity attackerEntity = new AttackerEntity(territory, player, unitType, UnitNo);
        return repository.save(attackerEntity);
    }

    @Override
    @Transactional
    public void clearAttackers(TerritoryEntity territory) {
        repository.deleteByTerritory(territory);
    }
}
