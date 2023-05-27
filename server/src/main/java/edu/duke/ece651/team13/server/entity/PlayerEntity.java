package edu.duke.ece651.team13.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.duke.ece651.team13.server.enums.PlayerStatusEnum;
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
import java.util.Objects;

/**
 * This class handles the information of one human player
 */
@Entity
@Table(name = "PLAYER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "playerSeq")
    @SequenceGenerator(name = "playerSeq")
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GAME_ID")
    @JsonBackReference
    private GameEntity game;

    @Column(name = "NAME", length = 50, nullable = false, unique = false)
    private String name;

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private PlayerStatusEnum status = PlayerStatusEnum.PLAYING;

    @Column(name = "FOOD_RESOURCE")
    private int foodResource; //food resource totals of this player

    @Column(name = "TECH_RESOURCE")
    private int techResource; //tech resource totals of this player

    @Column(name = "MAX_TECH_LEVEL")
    private int maxTechLevel; //tech level of this player

    @Column(name = "CLOAK_RESEARCHED")
    private boolean cloakResearched; //indicate whether the player has researched the cloak order

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private UserEntity user;

    /**
     * Construct a new Player
     */
    public PlayerEntity(String name) {
        this.name = name;
        this.status = PlayerStatusEnum.PLAYING;
        this.maxTechLevel = 1;
        this.cloakResearched =false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerEntity that = (PlayerEntity) o;
        return Id.equals(that.Id) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, name);
    }
}
