import React from "react";
import { Card } from "react-bootstrap";

const AttackToInfoCard = (props) => {

    const { source, territories, player } = props;

    if (!source) return;

    const getConnections = (name) => {
        return territories.find((territory) => territory.name === name).connections;
    };

    const getTerritory = (id) => {
        return territories.find((territory) => territory.id === id);
    };

    const listTerritories = getConnections(source).map((connection) =>
        <li key={connection.destinationTerritoryId}>
            <p style={territoryNameStyles}>
                {getTerritory(connection.destinationTerritoryId).name}
            </p>
        </li>
    );

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
        <Card>
            <Card.Body style={cardStyles}>
                <Card.Text>Make sure not to attack your own territory.</Card.Text>
                <Card.Text>Territories that are connected to <span style={territoryNameStyles}>{source}</span>:</Card.Text>
                <ul>
                    {listTerritories}
                </ul>
            </Card.Body>
        </Card>
    );
};

const territoryNameStyles = {
    fontWeight: "bold",
}

export default AttackToInfoCard;