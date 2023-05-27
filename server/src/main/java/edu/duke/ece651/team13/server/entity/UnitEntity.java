package edu.duke.ece651.team13.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Unit
 */
@Entity
@Table(name = "UNIT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unitSeq")
    @SequenceGenerator(name = "unitSeq")
    private Long Id;

    @Column(name = "UNIT_TYPE")
    @Enumerated(EnumType.STRING)
    private UnitMappingEnum unitType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TERRITORY_ID")
    @JsonBackReference
    private TerritoryEntity territory;

    @Column(name = "UNIT_NUM")
    private Integer unitNum;

    /**
     * Constructs a UnitEntity object with the given unit type and unit number.
     * @param unitType the type of the unit
     * @param unitNum the number of units of the given type
     */
    public UnitEntity(UnitMappingEnum unitType, Integer unitNum) {
        this.unitType = unitType;
        this.unitNum = unitNum;
    }
}
