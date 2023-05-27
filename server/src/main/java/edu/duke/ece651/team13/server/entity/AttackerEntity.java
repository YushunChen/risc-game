package edu.duke.ece651.team13.server.entity;

import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Attacker information
 */
@Entity
@Table(name = "ATTACKER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttackerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attackerSeq")
    @SequenceGenerator(name = "attackerSeq")
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "TERRITORY_ID", nullable = false)
    private TerritoryEntity territory;

    @ManyToOne
    @JoinColumn(name = "ATTACKER_PLAYER_ID", nullable = false)
    private PlayerEntity attacker;

    @Column(name = "UNIT_TYPE")
    @Enumerated(EnumType.STRING)
    private UnitMappingEnum unitType;

    @Column(name = "units")
    private Integer units;

    /**
     * Constructor for creating a new AttackerEntity with the given TerritoryEntity, PlayerEntity, UnitMappingEnum, and number of units.
     * @param territory the TerritoryEntity being attacked.
     * @param attacker the PlayerEntity launching the attack.
     * @param unitType the UnitMappingEnum representing the type of unit being used in the attack.
     * @param units the number of units being used in the attack.
     */
    public AttackerEntity(TerritoryEntity territory, PlayerEntity attacker, UnitMappingEnum unitType, Integer units) {
        this.territory = territory;
        this.attacker = attacker;
        this.unitType = unitType;
        this.units = units;
    }
}
