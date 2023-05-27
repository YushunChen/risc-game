import React from "react";
import "../styles/Map.css";
import Map2Players from "./components/Map2Players";
import Map4Players from "./components/Map4Players";
import Map3Players from "./components/Map3Players";
import map2PlayersBg from "./images/Map2Players.png";
import map3PlayersBg from "./images/Map3Players.png";
import map4PlayersBg from "./images/Map4Players.png";

const Map = (props) => {
  const territories = props.game.map.territories;
  const playerNum = props.game.players.length;

  const getBgImg = (playerNum) => {
    switch (playerNum) {
      case 2:
        return `url(${map2PlayersBg})`;
      case 3:
        return `url(${map3PlayersBg})`;
      default:
        return `url(${map4PlayersBg})`;
    }
  };

  const backgroundStyles = {
    backgroundImage: getBgImg(playerNum),
    backgroundRepeat: "no-repeat",
    backgroundSize: "cover",
    backgroundPosition: "50% 50%",
  };

  const map3PlayersStyles = {
    position: 'relative',
    left: '-50px',
    transform: 'scale(0.9)',
  }

  const map4PlayersStyles = {
    position: 'relative',
    top: '-150px',
    left: '-120px',
    transform: 'scale(0.7)',
    width: '120%',
  }

  if (playerNum === 2) {
    return (
        <div style={backgroundStyles}>
          <Map2Players
              player={props.player}
              territories={territories}
              handleSourceOrTarget={props.handleSourceOrTarget}
          />
        </div>
    );
  } else if (playerNum === 3) {
    return (
        <div style={{...backgroundStyles, ...map3PlayersStyles}}>
          <Map3Players
              player={props.player}
              territories={territories}
              handleSourceOrTarget={props.handleSourceOrTarget}
          />
        </div>
    );
  }
  return (
      <div style={{...backgroundStyles, ...map4PlayersStyles}}>
        <Map4Players
            player={props.player}
            territories={territories}
            handleSourceOrTarget={props.handleSourceOrTarget}
        />
      </div>
  );
};

export default Map;
