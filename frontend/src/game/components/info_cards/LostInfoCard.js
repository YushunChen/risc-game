import React from "react";
import { Card, Row, Col, Button } from "react-bootstrap";

const LostInfoCard = (props) => {

    const { player } = props;

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
        <><Card>
            <Card.Body style={cardStyles}>
                <Card.Text>You have already lost! You can only spectate the game now. Click the refresh button below to watch the lastest game.</Card.Text>
            </Card.Body>
        </Card>
            <Row className="text-center" style={{ marginTop: "80%" }}>
                <Col>
                    <Button
                        onClick={props.handleRefresh}
                        variant="success"
                        size="lg"
                        style={{ fontWeight: "bold" }}
                    >
                        Refresh
                    </Button>
                </Col>
            </Row></>

    );
};
export default LostInfoCard;