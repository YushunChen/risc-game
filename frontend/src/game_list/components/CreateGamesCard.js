import React, { useState } from "react";
import { useContext } from "react";
import { AuthContext } from "../../auth/AuthProvider";
import axios from "axios";
import { Card, Button, Modal } from "react-bootstrap";

const CreateGamesCard = () => {
    const { user } = useContext(AuthContext);
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const createGame = async (playerNum) => {
        try {
            const config = {
                headers: { Authorization: `Bearer ${user.accessToken}` }
            }
            let response = await axios.post(`createGame/${playerNum}`, {}, config);
            console.log(`New game created: ${response.data}`);
            handleShow();
        } catch (error) {
            console.log(error);
        }
    }

    const handleCreateTwoPlayers = async (e) => {
        e.preventDefault();
        await createGame(2);
    }

    const handleCreateThreePlayers = async (e) => {
        e.preventDefault();
        await createGame(3);
    }

    const handleCreateFourPlayers = async (e) => {
        e.preventDefault();
        await createGame(4);
    }

    return (<>
        <Card style={cardStyles}>
            <Card.Body>
                <Card.Title className="text-center" style={titleStyles}>
                    Create Games
                </Card.Title>
                <p className="mt-3 mb-0 text-center" style={textStyles}>Create a new game for other players to join!</p>
                <div className="text-center" style={{ marginTop: "10%" }} >
                    <Button onClick={handleCreateTwoPlayers} variant="primary" style={buttonStyles} size="lg" block="true">
                        2 Players
                    </Button>
                </div>
                <div className="text-center" style={{ marginTop: "10%" }} >
                    <Button onClick={handleCreateThreePlayers} variant="primary" style={buttonStyles} size="lg" block="true">
                        3 Players
                    </Button>
                </div>
                <div className="text-center" style={{ marginTop: "10%" }} >
                    <Button onClick={handleCreateFourPlayers} variant="primary" style={buttonStyles} size="lg" block="true">
                        4 Players
                    </Button>
                </div>
            </Card.Body>
        </Card>

        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title style={{ color: "green" }}>
                    Success!
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                New game has been created. Please find the game so you can join it!
            </Modal.Body>
        </Modal>
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

export default CreateGamesCard;