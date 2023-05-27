package edu.duke.ece651.team13.server.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.duke.ece651.team13.server.enums.GameStatusEnum;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Game information
 */
@Entity
@Table(name = "GAME")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gameSeq")
    @SequenceGenerator(name = "gameSeq")
    private Long Id;

    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PlayerEntity> players = new ArrayList<>();

    @OneToOne(mappedBy = "game", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private MapEntity map;

    @Column(name = "ROUND_NO")
    private int roundNo;

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private GameStatusEnum status = GameStatusEnum.PLAYING;

    /**
     * Returns a PlayerEntity object with the given ID.
     * Throws NoSuchElementException if no player with the given ID is found.
     * @param Id the ID of the player to retrieve.
     * @return a PlayerEntity object with the given ID.
     * @throws NoSuchElementException if no player with the given ID is found.
     */
    public PlayerEntity getPlayerEntityById(Long Id) {
        return players.stream().filter(playerEntity -> Objects.equals(playerEntity.getId(), Id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cannot find player entity with id " + Id));
    }
}