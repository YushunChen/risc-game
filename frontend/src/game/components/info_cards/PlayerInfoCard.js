import React from "react";
import { Row, Col, Card } from "react-bootstrap";
import { GiFruitBowl, Gi3DStairs } from "react-icons/gi";
import { HiOutlineDesktopComputer } from "react-icons/hi";

const PlayerInfoCard = (props) => {
  const { game, player } = props;
  console.log("Player ID: ", player.id);
  // TODO: Remove this dummy data
  // const player = {
  //   "name": "Red",
  //   "status": "PLAYING",
  //   "foodResource": 650,
  //   "techResource": 650,
  //   "maxTechLevel": 1,
  //   "id": 1
  // }
  const getCardColor = (owner) => {
    switch (owner) {
      case "Red":
        return "#FFCCCB";
      case "Blue":
        return "#ADD8E6";
      case "Green":
        return "#90EE90";
      case "Yellow":
        return "#FFFFE0";
      default:
        return "#F5F5F5";
    }
  };
  const playerInfoCardStyles = {
    backgroundColor: getCardColor(player.name),
    textAlign: "center",
    boxShadow: "0px 0px 10px rgba(0, 0, 0, 0.3)",
  };
  return (
    <Card>
      <Card.Body style={playerInfoCardStyles}>
        <Card.Title>Round {game.roundNo}</Card.Title>
        <Card.Text>You are the {player.name} player</Card.Text>
        <Row>
          <Col md={4}>
            <GiFruitBowl size={30} />
            <p>{player.foodResource}</p>
          </Col>
          <Col md={4}>
            <HiOutlineDesktopComputer size={30} />
            <p>{player.techResource}</p>
          </Col>
          <Col md={4}>
            <Gi3DStairs size={30} />
            <p>{player.maxTechLevel}</p>
          </Col>
        </Row>
      </Card.Body>
    </Card>
  )
};

export default PlayerInfoCard;
