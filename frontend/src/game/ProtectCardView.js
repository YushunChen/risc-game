import React, { useEffect, useState, useContext, useCallback } from "react";
import Map from "../maps/Map";
import GameBanner from "./components/GameBanner";
import PlayerInfoCard from "./components/info_cards/PlayerInfoCard";
import { Container, Row, Col } from "react-bootstrap";
import axios from "axios";
import ProtectCardInfoCard from "./components/info_cards/ProtectCardInfoCard";
import { AuthContext } from "../auth/AuthProvider";
import { useLocation } from "react-router-dom";
import LoadingView from "./components/LoadingView";
import ProtectCardModal from "./components/ProtectCardModal";

const ProtectCardView = () => {
  const { user } = useContext(AuthContext);
  const location = useLocation();
  const gameId = location.state.gameId;
  const [game, setGame] = useState();
  const [player, setPlayer] = React.useState();
  const [isLoading, setIsLoading] = useState(true);
  const [sourceTerritory, setSourceTerritory] = useState();

  const fetchGame = useCallback(async () => {
    try {
      const config = {
        headers: { Authorization: `Bearer ${user.accessToken}` },
      };
      let response = await axios.get(
        `getGameForUser/${gameId}?userId=${user.userId}`,
        config
      );
      console.log(`Current game: ${response.data.game}`);
      console.log(`Current player: ${response.data.player}`);
      setGame(response.data.game);
      setPlayer(response.data.player);
      setIsLoading(false);
    } catch (error) {
      console.log(error);
    }
  }, [gameId, user.accessToken, user.userId]);

  useEffect(() => {
    fetchGame();
  }, [fetchGame]);

  if (isLoading) {
    return <LoadingView />;
  }

  return (
    <>
      <Container>
        <Row>
          <Col md={9}>
            <GameBanner view="protect-card" />
            <Map
              game={game}
              player={player}
              handleSourceOrTarget={setSourceTerritory}
            />
          </Col>
          <Col md={3}>
            <PlayerInfoCard player={player} game={game} />
            <br />
            <ProtectCardInfoCard player={player} source={sourceTerritory} />
          </Col>
        </Row>
      </Container>
      <ProtectCardModal
        player={player}
        gameId={gameId}
        source={sourceTerritory}
        territories={game.map.territories}
        orderType="CARD_UNBREAKABLE_DEFENCE"
      />
    </>
  );
};

export default ProtectCardView;
