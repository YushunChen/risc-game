package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.TerritoryViewEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;
import edu.duke.ece651.team13.server.entity.UnitViewEntity;

public interface UnitViewService {
    UnitViewEntity initUnitView(TerritoryViewEntity territoryView, UnitEntity unit);

    UnitViewEntity updateUnitView(UnitViewEntity unitView);
}
