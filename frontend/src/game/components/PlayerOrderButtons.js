import React, { useContext, useCallback, useState } from "react";
import { Row, Col, Button, Modal } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { PlayerContext } from "../context/PlayerProvider";
import { OrderContext } from "../context/OrderProvider";
import { AuthContext } from "../../auth/AuthProvider";
import axios from "axios";

const PlayerOrderButtons = (props) => {
  const navigate = useNavigate();
  const { hasResearched, setHasDone, hasCloakResearched, cardsNum } =
    useContext(PlayerContext);
  const { orders, removeAllOrders } = useContext(OrderContext);
  const { user } = useContext(AuthContext);

  // for modals
  const [showSuccess, setShowSuccess] = useState(false);
  const handleCloseSuccess = () => setShowSuccess(false);
  const handleShowSuccess = () => setShowSuccess(true);
  const [showFailure, setShowFailure] = useState(false);
  const handleCloseFailure = () => setShowFailure(false);
  const handleShowFailure = () => setShowFailure(true);

  // order error messages
  const [errorMessage, setErrorMessage] = useState("");

  const handleAttack = () => {
    navigate("/attack", { state: { gameId: props.gameId } });
  };

  const handleMove = () => {
    navigate("/move", { state: { gameId: props.gameId } });
  };

  const handleResearch = () => {
    navigate("/research", { state: { gameId: props.gameId } });
  };

  const handleUpgrade = () => {
    navigate("/upgrade", { state: { gameId: props.gameId } });
  };

  const handleSpy = () => {
    navigate("/spy", {state: { gameId: props.gameId}});
  };

  const handleCloak = () => {
    if (hasCloakResearched) {
      navigate("/cloak", { state: { gameId: props.gameId } });
    } else {
      navigate("/researchCloak", { state: { gameId: props.gameId } });
    }
  };

  const handleDraw = () => {
    navigate("/card", { state: { gameId: props.gameId } });
  };

  const handleBack = () => {
    navigate("/gameList");
  };

  const handleDone = useCallback(async () => {
    try {
      const config = {
        headers: { Authorization: `Bearer ${user.accessToken}` },
      };
      const response = await axios.post(
        `submitOrder/?playerId=${props.player.id}`,
        {
          orders: orders.length === 0 ? [{ orderType: "DONE" }] : orders,
        },
        config
      );
      console.log(`Done response: ${response.data}`);
      setHasDone(true);
      handleShowSuccess();
    } catch (error) {
      console.log(error.response.data.message);
      setErrorMessage(error.response.data.message);
      handleShowFailure();
      removeAllOrders();
    }
  }, [props.player.id, user.accessToken, orders, setHasDone, removeAllOrders]);

  return (
    <>
      <Row className="text-center">
        <Col md={6}>
          <Button
            onClick={handleMove}
            className="rounded-circle"
            style={basicOrderButtonStyles}
            size="lg"
          >
            Move
          </Button>
        </Col>
        <Col md={6}>
          {cardsNum < 3 && (
            <Button
              onClick={handleDraw}
              className="rounded-circle"
              style={advancedOrderButtonStyles}
              size="lg"
            >
              Card
            </Button>
          )}
        </Col>
      </Row>

      <br />
      <Row className="text-center">
        <Col md={6}>
          <Button
            onClick={handleAttack}
            className="rounded-circle"
            style={basicOrderButtonStyles}
            size="lg"
          >
            Attack
          </Button>
        </Col>
        <Col md={6}>
          <Button
            onClick={handleSpy}
            className="rounded-circle"
            style={advancedOrderButtonStyles}
            size="lg"
          >
            Spy
          </Button>
        </Col>
      </Row>

      <br />
      <Row className="text-center">
        <Col md={6}>
          {!hasResearched && (
            <Button
              onClick={handleResearch}
              className="rounded-circle"
              style={basicOrderButtonStyles}
              size="lg"
            >
              Research
            </Button>
          )}
        </Col>
        {props.player.maxTechLevel >= 3 && (
          <Col md={6}>
            <Button
              onClick={handleCloak}
              className="rounded-circle"
              style={advancedOrderButtonStyles}
              size="lg"
            >
              Cloak
            </Button>
          </Col>
        )}
      </Row>

      <br />
      <Row className="text-center">
        <Col md={6}>
          <Button
            onClick={handleUpgrade}
            className="rounded-circle"
            style={basicOrderButtonStyles}
            size="lg"
          >
            Upgrade
          </Button>
        </Col>
      </Row>

      <Row className="text-center" style={{ marginTop: "80%" }}>
        <Col>
          <Button
            onClick={handleBack}
            variant="danger"
            size="lg"
            style={{ fontWeight: "bold" }}
          >
            Game List
          </Button>
        </Col>
        <Col>
          <Button
            onClick={handleDone}
            variant="success"
            size="lg"
            style={{ fontWeight: "bold" }}
          >
            Done
          </Button>
        </Col>
      </Row>

      <Modal show={showSuccess} onHide={handleCloseSuccess}>
        <Modal.Header closeButton>
          <Modal.Title style={{ color: "green" }}>Success!</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Your orders have been placed. Please wait for others to complete their
          orders...
        </Modal.Body>
      </Modal>

      <Modal show={showFailure} onHide={handleCloseFailure}>
        <Modal.Header closeButton>
          <Modal.Title style={{ color: "red" }}>Oops!</Modal.Title>
        </Modal.Header>
        <Modal.Body>The error is: {errorMessage}</Modal.Body>
      </Modal>
    </>
  );
};

const orderButtonStyles = {
  height: "4rem",
  width: "8rem",
  color: "white",
  fontWeight: "bold",
  outline: "none",
  border: "none",
  boxShadow: "0px 0px 10px rgba(0, 0, 0, 0.3)",
};

const basicOrderButtonStyles = {
  ...orderButtonStyles,
  backgroundColor: "#17A2B8",
};

const advancedOrderButtonStyles = {
  ...orderButtonStyles,
  backgroundColor: "#FFC107",
};

const upgradeButtonStyles = {
  ...orderButtonStyles,
  backgroundColor: "#A020F0",
};

export default PlayerOrderButtons;
