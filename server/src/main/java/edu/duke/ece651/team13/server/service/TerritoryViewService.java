package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.TerritoryViewEntity;

public interface TerritoryViewService {
    TerritoryViewEntity initTerritoryView(TerritoryEntity territory, PlayerEntity viewer);

    TerritoryViewEntity updateTerritoryView(TerritoryViewEntity territoryView);
}
