import React, { useContext } from "react";
import { Card, Row, Button, Col } from "react-bootstrap";
import { PlayerContext } from "../../context/PlayerProvider";
import { OrderContext } from "../../context/OrderProvider";
import { useNavigate } from "react-router-dom";

const ResearchInfoCard = (props) => {
    const navigate = useNavigate();
    const { setHasResearched } = useContext(PlayerContext);
    const { addOneOrder } = useContext(OrderContext);
    const { player } = props;
    const handleConfirm = () => {
        const order = {
            orderType: "TECH_RESEARCH"
        }
        addOneOrder(order);
        setHasResearched(true);
        navigate("/", { state: { gameId: props.gameId } });
    }
    const handleCancel = () => {
        navigate("/", { state: { gameId: props.gameId } });
    }
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
                    <Card.Text>You can only research and upgrade your maximum technology level once each turn.</Card.Text>
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
        </>
    );
};

export default ResearchInfoCard;