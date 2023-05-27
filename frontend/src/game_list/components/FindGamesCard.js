import React, { useState } from "react";
import { useContext } from "react";
import { AuthContext } from "../../auth/AuthProvider";
import axios from "axios";
import { Card, Button, Container, Row, Col } from "react-bootstrap";

const FindGamesCard = (props) => {
    const { user } = useContext(AuthContext);
    const [freeGames, setFreeGames] = useState([]);
    const config = {
        headers: { Authorization: `Bearer ${user.accessToken}` }
    }

    const findFreeGames = async () => {
        console.log(user.accessToken)
        try {
            let response = await axios.get(`getFreeGames?userId=${user.userId}`, config);
            console.log(`Free games: ${response.data}`);
            setFreeGames(response.data.games);
        } catch (error) {
            console.log(error);
        }
    }

    const handleFind = async (e) => {
        e.preventDefault();
        await findFreeGames();
    }

    const handleJoin = async (gameId) => {
        try {
            let response = await axios.post(`joinGame/${gameId}?userId=${user.userId}`, {}, config);
            console.log(`Joined a new game: ${response.data}`);
            const updatedGamePlayerMap = { ...props.gamePlayerMap };
            updatedGamePlayerMap[gameId] = response.data;
            props.setGamePlayerMap(updatedGamePlayerMap);
            await findFreeGames();
            await props.fetchUserGames();
        } catch (error) {
            console.log(error);
        }
    }

    return (<>
        <Card style={cardStyles}>
            <Card.Body>
                <Card.Title className="text-center" style={titleStyles}>
                    Find Games
                </Card.Title>
                <p className="mt-3 mb-0 text-center" style={textStyles}>Find free games to join so you can play with other players!</p>
                <br />
                <Container>
                    {freeGames.map((game) => (
                        <Row key={game.id} style={{ height: "3rem" }}>
                            <Col>Game ID: {game.id}</Col>
                            <Col><Button onClick={() => handleJoin(game.id)} style={joinButtonStyles}>Join</Button></Col>
                        </Row>
                    ))}
                </Container>
                <div className="text-center" style={{ marginTop: "10%" }} >
                    <Button onClick={handleFind} variant="primary" style={buttonStyles} size="lg" block="true">
                        Find
                    </Button>
                </div>
            </Card.Body>
        </Card>
    </>
    )
}

const cardStyles = {
    width: "20rem",
    minHeight: "30rem",
    borderRadius: "2rem",
    boxShadow: "0 0 10px rgba(0, 0, 0, 0.6)",
};
const titleStyles = {
    fontSize: "2rem",
    color: "#77A6F7",
};
const textStyles = {
    color: "#BEBCBC"
}
const buttonStyles = {
    backgroundColor: "#77A6F7",
    fontWeight: "bold",
    outline: "none",
    border: "none",
    borderRadius: "40px",
    width: "8rem"
}

const joinButtonStyles = {
    ...buttonStyles,
    width: "6rem",
    height: "2rem",
    fontWeight: "normal",
}

export default FindGamesCard;