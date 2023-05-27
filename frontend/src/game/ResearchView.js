import React, { useEffect, useState, useContext, useCallback } from "react";
import Map from "../maps/Map";
import GameBanner from "./components/GameBanner";
import PlayerInfoCard from "./components/info_cards/PlayerInfoCard";
import { Container, Row, Col } from "react-bootstrap";
import axios from "axios";
import ResearchInfoCard from "./components/info_cards/ResearchInfoCard";
import { AuthContext } from "../auth/AuthProvider";
import { useLocation } from 'react-router-dom';
import LoadingView from "./components/LoadingView";

const ResearchView = () => {
    const { user } = useContext(AuthContext);
    const location = useLocation();
    const gameId = location.state.gameId;
    const [game, setGame] = useState();
    const [player, setPlayer] = React.useState();
    const [isLoading, setIsLoading] = useState(true);

    const fetchGame = useCallback(async () => {
        try {
            const config = {
                headers: { Authorization: `Bearer ${user.accessToken}` }
            }
            let response = await axios.get(`getGameForUser/${gameId}?userId=${user.userId}`, config);
            console.log(`Current game: ${response.data.game}`);
            console.log(`Current player: ${response.data.player}`);
            setGame(response.data.game);
            setPlayer(response.data.player)
            setIsLoading(false);
        } catch (error) {
            console.log(error);
        }
    }, [gameId, user.accessToken, user.userId])

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
                        <GameBanner view="research" />
                        <Map game={game} player={player} />
                    </Col>
                    <Col md={3}>
                        <PlayerInfoCard player={player} game={game} />
                        <br />
                        <ResearchInfoCard player={player} gameId={gameId} />
                    </Col>
                </Row>
            </Container>
        </>
    );
};

export default ResearchView;