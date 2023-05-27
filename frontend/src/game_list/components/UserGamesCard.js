import React from "react";
import { Card, Button, Container, Row, Col } from "react-bootstrap";
import { useNavigate } from 'react-router-dom';

const UserGamesCard = (props) => {
    const navigate = useNavigate();
    const handlePlay = async (gameId) => {
        navigate('/', { state: { gameId, gamePlayerMap: props.gamePlayerMap } });
        console.log(gameId);
    }
    return (
        <Card style={cardStyles}>
            <Card.Body>
                <Card.Title className="text-center" style={titleStyles}>
                    Your Games
                </Card.Title>
                <p className="mt-3 mb-0 text-center" style={textStyles}>Here are the games you have already joined and played in:</p>
                <br />
                <Container>
                    {props.userGames.map((game) => (
                        <Row key={game.id} style={{ height: "3rem" }}>
                            <Col>Game ID: {game.id}</Col>
                            <Col><Button onClick={() => handlePlay(game.id)} style={playButtonStyles}>Play</Button></Col>
                        </Row>
                    ))}
                </Container>
            </Card.Body>
        </Card>)
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

const playButtonStyles = {
    ...buttonStyles,
    width: "6rem",
    height: "2rem",
    fontWeight: "normal",
    backgroundColor: "#28A745",
}

export default UserGamesCard;