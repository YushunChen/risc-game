import React, { useState, useContext } from "react";
import { Card, Row, Button, Col } from "react-bootstrap";
import { PlayerContext } from "../../context/PlayerProvider";
import { useNavigate } from "react-router-dom";
import CardModal from "../CardModal";

const CardInfoCard = (props) => {
  const navigate = useNavigate();
  const { cardsNum, setCardsNum } = useContext(PlayerContext);
  const { player, gameId, territories } = props;
  const [orderType, setOrderType] = useState();

  const handleConfirm = () => {
    drawCard();
  };
  const handleCancel = () => {
    navigate("/", { state: { gameId: props.gameId } });
  };

  const drawCard = () => {
    const cardOrderTypes = [
      { type: "CARD_CONQUERING_WARRIORS", weight: 0.1 },
      { type: "CARD_FAMINE", weight: 0.1 },
      { type: "CARD_UNBREAKABLE_DEFENCE", weight: 0.1 },
      { type: "CARD_NO_LUCK", weight: 0.7 },
    ];
    // select a random card type based on the weights
    const weightedRandomSelect = () => {
      const weightedArray = cardOrderTypes.flatMap(({ type, weight }) =>
        Array(Math.round(weight * 100)).fill(type)
      );
      const randomIndex = Math.floor(Math.random() * weightedArray.length);
      return weightedArray[randomIndex];
    };
    const randomCardOrderType = weightedRandomSelect(cardOrderTypes);
    console.log(randomCardOrderType);
    setOrderType(randomCardOrderType);
    setCardsNum(cardsNum + 1);
  };

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
  const cardStyles = {
    backgroundColor: getCardColor(player.name),
    textAlign: "left",
    boxShadow: "0px 0px 10px rgba(0, 0, 0, 0.3)",
  };

  return (
    <>
      <Card>
        <Card.Body style={cardStyles}>
          <Card.Text>
            You are about to draw a random card that can have some effects on
            your game. The effects may either be positive or negative. You can
            only draw 3 cards per round.
          </Card.Text>
        </Card.Body>
      </Card>
      <Row className="text-center" style={{ marginTop: "80%" }}>
        <Col>
          <Button
            onClick={handleCancel}
            variant="danger"
            size="lg"
            style={{ fontWeight: "bold" }}
          >
            Cancel
          </Button>
        </Col>
        <Col>
          <Button
            onClick={handleConfirm}
            variant="success"
            size="lg"
            style={{ fontWeight: "bold" }}
          >
            Confirm
          </Button>
        </Col>
      </Row>

      <CardModal
        player={player}
        gameId={gameId}
        territories={territories}
        orderType={orderType}
      />
    </>
  );
};

export default CardInfoCard;
