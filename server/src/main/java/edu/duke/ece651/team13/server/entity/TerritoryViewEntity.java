package edu.duke.ece651.team13.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.duke.ece651.team13.server.enums.TerritoryDisplayEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TERRITORY_VIEW")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TerritoryViewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "territoryViewSeq")
    @SequenceGenerator(name = "territoryViewSeq")
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VIEWER_ID")
    @JsonManagedReference
    private PlayerEntity viewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TERRITORY_ID")
    @JsonBackReference
    private TerritoryEntity toDisplay;

    /**
     * if INVISIBLE -> only the outline should be displayed
     * if VISIBLE_NEW -> display info stored in this territoryViewEntity using appropriate colors
     * if VISIBLE_OLD -> display info stored in this territoryViewEntity with clear indicate that info is old (e.g., gray coloring)
     */
    @Column(name = "DISPLAY_TYPE")
    @Enumerated(EnumType.STRING)
    private TerritoryDisplayEnum displayType;

    @Column(name = "VISIBLE_BEFORE")
    private boolean visibleBefore; //indicate whether this territory has been seen before by the viewer

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_DISPLAY_ID")
    @JsonManagedReference
    private PlayerEntity ownerDisplay;

    @OneToMany(mappedBy = "territoryView", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UnitViewEntity> unitsDisplay = new ArrayList<>(); //size=7; used to indicate the number of different types of unit (level0~level6);
}
