import Modal from "react-bootstrap/Modal";
import { Button } from "react-bootstrap";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useContext } from "react";
import { OrderContext } from "../context/OrderProvider";

const ProtectCardModal = (props) => {
  const { addOneOrder } = useContext(OrderContext);

  const sourceName = props.source;
  const territories = props.territories;
  const getTerritory = (name) => {
    return territories.find((territory) => territory.name === name);
  };

  const navigate = useNavigate();
  const [showModal, setShowModal] = useState(true);
  const closeModal = () => {
    setShowModal(false);
    navigate("/", { state: { gameId: props.gameId } });
  };

  const handleConfirmOrder = (e) => {
    e.preventDefault();
    addOneOrder({
      sourceTerritoryId: getTerritory(sourceName).id,
      orderType: props.orderType,
    });
    setShowModal(false);
    navigate("/", { state: { gameId: props.gameId } });
  };

  if (!sourceName) return;

  return (
    <>
      <Modal show={showModal}>
        <Modal.Header>
          <Modal.Title>
            Making {props.orderType} order in {sourceName}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>Your territory {sourceName} will have unbreakable defense next round!</p>
          <div style={{ display: "flex", justifyContent: "center" }}>
            <Button
              onClick={handleConfirmOrder}
              style={confirmButtonStyles}
              size="lg"
            >
              Confirm
            </Button>
            <Button onClick={closeModal} style={cancelButtonStyles} size="lg">
              Cancel
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

const cancelButtonStyles = {
  ...confirmButtonStyles,
  backgroundColor: "#D33431",
};

export default ProtectCardModal;
