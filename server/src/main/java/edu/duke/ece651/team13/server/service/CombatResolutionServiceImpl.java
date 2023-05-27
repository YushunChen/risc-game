package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.AttackerEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import edu.duke.ece651.team13.server.util.Dice;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CombatResolutionServiceImpl implements CombatResolutionService {

    @Autowired
    public final AttackerService attackerService;

    @Autowired
    public final TerritoryService territoryService;

    @Autowired
    public final UnitService unitService;

    @Autowired
    private final Dice dice;

    //Making public to test
    public void reduceUnit(List<MutablePair<UnitMappingEnum, Integer>> units, UnitMappingEnum unitType) {
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).getLeft().equals(unitType)) {
                int updatedValue = units.get(i).getRight() - 1;
                if (updatedValue <= 0) {
                    units.remove(i);
                } else {
                    units.get(i).setRight(updatedValue);
                }
                return;
            }
        }
    }

    //Making public to test
    public void fight(List<MutablePair<UnitMappingEnum, Integer>> attacker, List<MutablePair<UnitMappingEnum, Integer>> defender, Boolean isAttackerTerritoryOwner, Boolean isDefenderTerritoryOwner) {

        UnitMappingEnum attackerUnitType = attacker.stream()
                .map(MutablePair::getLeft)
                .max((Comparator.comparing(UnitMappingEnum::getLevel)))
                .orElseThrow(NoSuchElementException::new);

        UnitMappingEnum defenderUnitType = defender.stream()
                .map(MutablePair::getLeft)
                .min((Comparator.comparing(UnitMappingEnum::getLevel)))
                .orElseThrow(NoSuchElementException::new);

        while (true) {
            int attackerScore = dice.roll() + attackerUnitType.getBonus();
            int defenderScore = dice.roll() + defenderUnitType.getBonus();

            if (attackerScore > defenderScore) {
                //defender lost
                reduceUnit(defender, defenderUnitType);
                return;
            }

            if (attackerScore < defenderScore) {
                //attacker lost
                reduceUnit(attacker, attackerUnitType);
                return;
            }

            //Tie is Scored. Hence, Advantage goes to Territory Owner

            if (isAttackerTerritoryOwner) {
                //defender lost
                reduceUnit(defender, defenderUnitType);
                return;
            }

            if (isDefenderTerritoryOwner) {
                //attacker lost
                reduceUnit(attacker, attackerUnitType);
                return;
            }
        }
    }

    public void addUnitsToMutablePairList(List<MutablePair<UnitMappingEnum, Integer>> unitPairs, UnitMappingEnum unitType, Integer unitNum ){
        for (MutablePair<UnitMappingEnum, Integer> unitPair : unitPairs) {
            if (unitPair.getLeft().equals(unitType)) {
                //Appending to the already existing units
                unitPair.setRight(unitPair.getRight() + unitNum);
                return;
            }
        }

        //If the Unit pair does not exist then create a new entry in the list.
        unitPairs.add(new MutablePair<UnitMappingEnum, Integer>(unitType, unitNum));
    }

    //Making public to test
    public Map<PlayerEntity, List<MutablePair<UnitMappingEnum, Integer>>> getWarParties(List<AttackerEntity> attackers, TerritoryEntity territory) {
        Map<PlayerEntity, List<MutablePair<UnitMappingEnum, Integer>>> warParties = new HashMap<>();

        //Add all attackers to the war party
        for (AttackerEntity attacker : attackers) {
            if(attacker.getUnits() >0 ) { //Add to the list only if the attacker has units
                PlayerEntity player = attacker.getAttacker();
                if (!warParties.containsKey(player)) {
                    warParties.put(player, new ArrayList<>());
                }
                addUnitsToMutablePairList(warParties.get(player), attacker.getUnitType(), attacker.getUnits());
            }
        }

        //Adding the defender to the war party
        PlayerEntity defender = territory.getOwner();
        warParties.put(defender, new ArrayList<>());
        for (UnitEntity unit : territory.getUnits()) {
            if(unit.getUnitNum() > 0){
                addUnitsToMutablePairList(warParties.get(defender), unit.getUnitType(), unit.getUnitNum());
            }
        }

        //If the defender has no units then removing him from the war parties list.
        if(warParties.get(defender).size() == 0){
            warParties.remove(defender);
        }

        return warParties;
    }

    //Making public to test
    public Map.Entry<PlayerEntity, List<MutablePair<UnitMappingEnum, Integer>>> resolveWinner(TerritoryEntity territory, List<AttackerEntity> attackers) {
        Map<PlayerEntity, List<MutablePair<UnitMappingEnum, Integer>>> warPartyMap = getWarParties(attackers, territory);

        List<PlayerEntity> warParties = new ArrayList<>(warPartyMap.keySet());

        int i = 0;
        int j = 1;

        while (warParties.size() > 1) {

            PlayerEntity attacker = warParties.get(i);
            PlayerEntity defender = warParties.get(j);

            Boolean isAttackerTerritoryOwner = territory.getOwner().equals(attacker);
            Boolean isDefenderTerritoryOwner = territory.getOwner().equals(defender);

            fight(warPartyMap.get(attacker), warPartyMap.get(defender), isAttackerTerritoryOwner, isDefenderTerritoryOwner);

            if (warPartyMap.get(attacker).size() <= 0) {
                warParties.remove(i);
            } else if (warPartyMap.get(defender).size() <= 0) {
                warParties.remove(j);
            }

            i = (i + 1) % warParties.size();
            j = (j + 1) % warParties.size();
        }

        for (Map.Entry<PlayerEntity, List<MutablePair<UnitMappingEnum, Integer>>> entry : warPartyMap.entrySet()) {
            if (warParties.contains(entry.getKey())) {
                return entry;
            }
        }

        throw new NoSuchElementException();
    }

    @Override
    public void resolveCombot(TerritoryEntity territory) {
        List<AttackerEntity> attackers = attackerService.getAttackers(territory);
        //No attackers for this territory
        if (attackers.size() == 0) {
            return;
        }

        Map.Entry<PlayerEntity, List<MutablePair<UnitMappingEnum, Integer>>> winner = resolveWinner(territory, attackers);

        Map<UnitMappingEnum, Integer> unitMapping = winner.getValue().stream().collect(Collectors.toMap(MutablePair::getLeft, MutablePair::getRight));
        territory.setOwner(winner.getKey());
        List<UnitEntity> unitEntities = new ArrayList<>();
        for (UnitEntity unit : territory.getUnits()) {
            unit.setUnitNum(unitMapping.getOrDefault(unit.getUnitType(), 0));
            unitEntities.add(unit);
        }

        territoryService.updateTerritoryOwner(territory, winner.getKey());
        territoryService.updateTerritoryUnits(territory, unitEntities);

        attackerService.clearAttackers(territory);
    }
}
