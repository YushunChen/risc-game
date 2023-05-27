import React, { useState, useEffect, useCallback } from "react";
import { useContext } from "react";
import { AuthContext } from "../auth/AuthProvider";
import axios from "axios";
import { Container, Row, Col } from "react-bootstrap";
import CreateGamesCard from "./components/CreateGamesCard";
import FindGamesCard from "./components/FindGamesCard";
import UserGamesCard from "./components/UserGamesCard";
import LoadingView from "../game/components/LoadingView";

const GameListView = () => {
  const { user } = useContext(AuthContext);
  console.log("auth: " + user.accessToken);
  console.log("user: " + user.userId);
  const [userGames, setUserGames] = useState([]);
  const [isLoading, setIsLoading] = React.useState(true);
  const [gamePlayerMap, setGamePlayerMap] = useState({});

  const fetchUserGames = useCallback(async () => {
    console.log(user.accessToken)
    try {
      const config = {
        headers: { Authorization: `Bearer ${user.accessToken}` }
      }
      let response = await axios.get(`userGames?userId=${user.userId}`, config);
      console.log(`User games: ${response.data}`);
      setUserGames(response.data.games);
      setIsLoading(false);
    } catch (error) {
      console.log(error);
    }
  }, [user.accessToken, user.userId]);

  useEffect(() => {
    fetchUserGames();
  }, [fetchUserGames]);

  if (isLoading) {
    return <LoadingView />;
  }


  return (
    <Container className="vh-100 d-flex align-items-center justify-content-center">
      <Row>
        <Col>
          <UserGamesCard userGames={userGames} gamePlayerMap={gamePlayerMap} />
        </Col>
        <Col>
          <FindGamesCard fetchUserGames={fetchUserGames} gamePlayerMap={gamePlayerMap} setGamePlayerMap={setGamePlayerMap} />
        </Col>
        <Col>
          <CreateGamesCard />
        </Col>
      </Row>
    </Container >
  );
};

export default GameListView;
