package edu.duke.ece651.team13.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * SPY Unit
 */
@Entity
@Table(name = "SPY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpyUnitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unitSeq")
    @SequenceGenerator(name = "spySeq")
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "TERRITORY_ID", nullable = false)
    @JsonBackReference
    private TerritoryEntity territory;

    @Column(name = "SPY_NUM")
    private Integer unitNum;

    @ManyToOne
    @JoinColumn(name = "OWNER_PLAYER_ID", nullable = false)
    private PlayerEntity owner;

    public SpyUnitEntity(Integer unitNum, PlayerEntity player){
        this.unitNum = unitNum;
        this.owner = player;
    }
}
