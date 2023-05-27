import Modal from "react-bootstrap/Modal";
import { Button } from "react-bootstrap";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useContext } from "react";
import { OrderContext } from "../context/OrderProvider";

const CardModal = (props) => {
  const { addOneOrder } = useContext(OrderContext);

  const { gameId, orderType } = props;

  const navigate = useNavigate();
  const [showModal, setShowModal] = useState(true);

  const handleConfirmOrder = (e) => {
    e.preventDefault();
    if (orderType !== "CARD_UNBREAKABLE_DEFENCE") {
      addOneOrder({
        orderType,
      });
      setShowModal(false);
      navigate("/", { state: { gameId } });
    } else {
      // CARD_UNBREAKABLE_DEFENCE needs a source territory
      navigate("/protectCard", { state: { gameId } });
    }
  };

  const getCardMessage = (orderType) => {
    switch (orderType) {
      case "CARD_CONQUERING_WARRIORS":
        return "Hooray! Some of the best conquering warriors are on their way. You'll have some level 6 units in all your territories starting next round.";
      case "CARD_FAMINE":
        return "Oh no! A famine has struck your territories. Your food and technology production will be stopped for the round.";
      case "CARD_UNBREAKABLE_DEFENCE":
        return "Hooray! You can choose a territory of yours to be invincible for the next round.";
      case "CARD_NO_LUCK":
        return "Emm... This card doesn't do anything. Better luck next time!";
      default:
        return "Unknown card";
    }
  };

  if (!orderType) return;

  return (
    <>
      <Modal show={showModal}>
        <Modal.Header>
          <Modal.Title>Your card drawn: {orderType}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>{getCardMessage(orderType)}</p>
          <div style={{ display: "flex", justifyContent: "center" }}>
            <Button
              onClick={handleConfirmOrder}
              style={confirmButtonStyles}
              size="lg"
            >
              Confirm
            </Button>
          </div>
        </Modal.Body>
      </Modal>
    </>
  );
};

const confirmButtonStyles = {
  backgroundColor: "#26BC26",
  marginRight: "50px",
  border: "none",
};

export default CardModal;
