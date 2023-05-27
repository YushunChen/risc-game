package edu.duke.ece651.team13.server;

import edu.duke.ece651.team13.server.entity.AttackerEntity;
import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.MapEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;
import edu.duke.ece651.team13.server.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.LEVEL0;

public class MockDataUtil {
    private MockDataUtil() {
    }

    public static GameEntity getGameEntity() {
        GameEntity game = new GameEntity();
        game.setMap(getMapEntity());
        return game;
    }

    public static UserEntity getUserEntity() {
        return new UserEntity();
    }

    public static PlayerEntity getPlayerEntity() {
        return new PlayerEntity("Red");
    }

    public static MapEntity getMapEntity() {
        MapEntity map = new MapEntity();
        List<TerritoryEntity> territoryEntityList = new ArrayList<>();
        for (long i = 0; i < 5; i++) {
            TerritoryEntity territory = getTerritoryEntity();
            territory.setId(i);
            territoryEntityList.add(territory);
        }
        map.setTerritories(territoryEntityList);
        return map;
    }

    public static TerritoryEntity getTerritoryEntity() {
        TerritoryEntity territory = new TerritoryEntity();
        territory.getUnits().add(getUnitEntity(10));
        return territory;
    }

    public static UnitEntity getUnitEntity(int unitNum) {
        UnitEntity basicUnit = new UnitEntity();
        basicUnit.setUnitNum(unitNum);
        basicUnit.setUnitType(LEVEL0);
        return basicUnit;
    }

    public static AttackerEntity getAttackerEntity(TerritoryEntity territory) {
        return new AttackerEntity(territory, getPlayerEntity(), LEVEL0, 5);
    }
}

