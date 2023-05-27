import React from "react";
import TerritoryView from "./TerritoryView";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Container from "react-bootstrap/Container";

const Map2Players = (props) => {
  const { territories, player } = props;
  // get the territory to display
  const getTerritory = (name) => {
    return territories.find((territory) => territory.name === name);
  };

  return (
    <Container>
      <br />
      <br />
      <Row>
        <Col md={3}>
          <TerritoryView
            key="Rottweiler"
            territory={getTerritory("Rottweiler")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col md={1}> </Col>
        <Col md={3}>
          <TerritoryView
            key="Dachshund"
            territory={getTerritory("Dachshund")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col md={1}> </Col>
        <Col md={3}>
          <TerritoryView
            key="Beagle"
            territory={getTerritory("Beagle")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
      </Row>
      <br />
      <br />
      <Row>
        <Col md={3}>
          <TerritoryView
            key="Labrador"
            territory={getTerritory("Labrador")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col md={1}> </Col>
        <Col md={3}>
          <TerritoryView
            key="Poodle"
            territory={getTerritory("Poodle")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col md={1}> </Col>
        <Col md={3}>
          <TerritoryView
            key="Bulldog"
            territory={getTerritory("Bulldog")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
      </Row>
      <br />
      <br />
      <Row>
        <Col md={3}>
          <TerritoryView
            key="Boxer"
            territory={getTerritory("Boxer")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col md={1}> </Col>
        <Col md={3}>
          <TerritoryView
            key="Havanese"
            territory={getTerritory("Havanese")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col md={1}> </Col>
        <Col md={3}>
          <TerritoryView
            key="Spaniel"
            territory={getTerritory("Spaniel")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
      </Row>
      <br />
      <br />
      <Row>
        <Col md={3}>
          <TerritoryView
            key="Sheepdog"
            territory={getTerritory("Sheepdog")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col md={1}> </Col>
        <Col md={3}>
          <TerritoryView
            key="Akita"
            territory={getTerritory("Akita")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col md={1}> </Col>
        <Col md={3}>
          <TerritoryView
            key="Brittany"
            territory={getTerritory("Brittany")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
      </Row>
    </Container>
  );
};

export default Map2Players;
