package edu.duke.ece651.team13.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.duke.ece651.team13.server.enums.OrderMappingEnum;
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
 * Order
 */
@Entity
@Table(name = "GAME_ORDER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSeq")
    @SequenceGenerator(name = "orderSeq")
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAYER_ID")
    @JsonManagedReference
    private PlayerEntity player;

    @Column(name = "ORDER_TYPE")
    @Enumerated(EnumType.STRING)
    private OrderMappingEnum orderType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOURCE_TERRITORY_ID")
    @JsonIgnore
    private TerritoryEntity source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DESTINATION_TERRITORY_ID")
    @JsonIgnore
    private TerritoryEntity destination;

    @Column(name = "UNIT_NUMBER")
    private int unitNum;

    @Column(name = "UNIT_TYPE")
    @Enumerated(EnumType.STRING)
    private UnitMappingEnum unitType;
}