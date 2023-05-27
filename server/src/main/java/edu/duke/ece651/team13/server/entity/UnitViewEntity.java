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

@Entity
@Table(name = "UNIT_VIEW")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnitViewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unitViewSeq")
    @SequenceGenerator(name = "unitViewSeq")
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TERRITORY_VIEW_ID")
    @JsonBackReference
    private TerritoryViewEntity territoryView;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNIT_ID")
    @JsonBackReference
    private UnitEntity toDisplay;

    @Column(name = "UNIT_TYPE")
    @Enumerated(EnumType.STRING)
    private UnitMappingEnum unitType;

    @Column(name = "UNIT_NUM")
    private Integer unitNum;
}
