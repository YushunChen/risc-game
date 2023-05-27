import React from "react";
import { Card, Row, Col, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const WinInfoCard = (props) => {

    const { player } = props;
    const navigate = useNavigate();

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

    const handleRedirect = () => {
        navigate("/gameList");
    };

    return (
        <><Card>
            <Card.Body style={cardStyles}>
                <Card.Text>Congratulations! You have won! Feel free to start a new game.</Card.Text>
            </Card.Body>
        </Card>
            <Row className="text-center" style={{ marginTop: "80%" }}>
                <Col>
                    <Button
                        onClick={handleRedirect}
                        variant="danger"
                        size="lg"
                        style={{ fontWeight: "bold" }}
                    >
                        Game List
                    </Button>
                </Col>
            </Row></>

    );
};
export default WinInfoCard;