package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryConnectionEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.TerritoryViewEntity;
import edu.duke.ece651.team13.server.entity.UnitViewEntity;
import edu.duke.ece651.team13.server.repository.TerritoryViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static edu.duke.ece651.team13.server.enums.TerritoryDisplayEnum.INVISIBLE;
import static edu.duke.ece651.team13.server.enums.TerritoryDisplayEnum.VISIBLE_NEW;
import static edu.duke.ece651.team13.server.enums.TerritoryDisplayEnum.VISIBLE_OLD;

@Service
@RequiredArgsConstructor
public class TerritoryViewServiceImpl implements TerritoryViewService{
    @Autowired
    private final TerritoryViewRepository repository;

    @Autowired
    private final UnitViewService unitViewService;

    @Override
    @Transactional
    public TerritoryViewEntity initTerritoryView(TerritoryEntity toDisplay, PlayerEntity viewer){
        TerritoryViewEntity territoryView = new TerritoryViewEntity();
        territoryView.setToDisplay(toDisplay);
        toDisplay.getTerritoryViews().add(territoryView);
        territoryView.setViewer(viewer);
        territoryView.setOwnerDisplay(toDisplay.getOwner());
        if(isVisible(toDisplay, viewer)) territoryView.setDisplayType(VISIBLE_NEW);
        else territoryView.setDisplayType(INVISIBLE);
        return repository.save(territoryView);
    }

    /**
     * INVISIBLE to VISIBLE_NEW; VISIBLE_NEW to VISIBLE_OLD; VISIBLE_OLD to VISIBLE_NEW
     * VISIBLE_NEW to INVISIBLE; VISIBLE_OLD to INVISIBLE
     * @param territoryView territoryView to be updated
     * @return update-to-update territoryView
     */
    @Override
    @Transactional
    public TerritoryViewEntity updateTerritoryView(TerritoryViewEntity territoryView){
        // from any type of territoryDisplayEnum to VISIBLE_NEW
        if(isVisible(territoryView.getToDisplay(), territoryView.getViewer())){
            territoryView.setDisplayType(VISIBLE_NEW);
            territoryView.setVisibleBefore(true);
            territoryView.setOwnerDisplay(territoryView.getToDisplay().getOwner());
            for(int i=0; i<territoryView.getUnitsDisplay().size(); i++){
                UnitViewEntity unitView = territoryView.getUnitsDisplay().get(i);
                unitViewService.updateUnitView(unitView);
            }
        }
        else{
            // from any type of territoryDisplayEnum to INVISIBLE
            if(territoryView.getToDisplay().getRemainingCloak()>0 || !territoryView.isVisibleBefore()){
                territoryView.setDisplayType(INVISIBLE);
            }
            // from any type of territoryDisplayEnum to VISIBLE_OLD (territory should be seen before)
            else territoryView.setDisplayType(VISIBLE_OLD);
        }
        return repository.save(territoryView);
    }

    /**
     * check whether the territory is visible to the viewer:
     * -> whether territory belongs to the viewer or contains spy from viewer or is cloaked
     * -> or is an immediately adjacent enemy territory to viewer
     * @param territory territory
     * @param viewer viewer
     * @return true if it is, otherwise false
     */
    private boolean isVisible(TerritoryEntity territory, PlayerEntity viewer){
        if(territory.getOwner().equals(viewer)) return true;
        if(territory.getSpyForPlayer(viewer).getUnitNum() > 0) return true;
        if(territory.getRemainingCloak()>0) return false;
        for(TerritoryConnectionEntity t: territory.getConnections()){
            if(t.getDestinationTerritory().getOwner().equals(viewer)) return true;
        }
        return false;
    }
}
