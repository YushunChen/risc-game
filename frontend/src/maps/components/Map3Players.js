import React from "react";
import TerritoryView from "./TerritoryView";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Container from "react-bootstrap/Container";

const Map3Players = (props) => {
  const { territories, player } = props;
  const getTerritory = (name) => {
    return territories.find((territory) => territory.name === name);
  };

  return (
    <Container>
      <Row>
        <Col></Col>
        <Col></Col>
        <Col>
          <TerritoryView
            key="Labrador"
            territory={getTerritory("Labrador")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col></Col>
        <Col>
          <TerritoryView
            key="Bulldog"
            territory={getTerritory("Bulldog")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col></Col>
        <Col></Col>
      </Row>
      <br />
      <Row>
        <Col></Col>
        <Col></Col>
        <Col>
          <TerritoryView
            key="Rottweiler"
            territory={getTerritory("Rottweiler")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col md={1}> </Col>
        <Col>
          <TerritoryView
            key="Dachshund"
            territory={getTerritory("Dachshund")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col md={1}> </Col>
        <Col>
          <TerritoryView
            key="Beagle"
            territory={getTerritory("Beagle")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col></Col>
        <Col></Col>
      </Row>
      <br />
      <Row>
        <Col md={1}>
          <TerritoryView
            key="Boxer"
            territory={getTerritory("Boxer")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col></Col>
        <Col>
          <TerritoryView
            key="Spaniel"
            territory={getTerritory("Spaniel")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col> </Col>
        <Col md={1}>
          <TerritoryView
            key="Poodle"
            territory={getTerritory("Poodle")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col> </Col>
        <Col> </Col>
        <Col md={1}>
          <TerritoryView
            key="Pug"
            territory={getTerritory("Pug")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col> </Col>
        <Col md={1}>
          <TerritoryView
            key="Mastiff"
            territory={getTerritory("Mastiff")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
      </Row>
      <br />
      <Row>
        <Col></Col>
        <Col></Col>
        <Col>
          <TerritoryView
            key="Brittany"
            territory={getTerritory("Brittany")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col> </Col>
        <Col>
          <TerritoryView
            key="Havanese"
            territory={getTerritory("Havanese")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col> </Col>
        <Col>
          <TerritoryView
            key="Vizsla"
            territory={getTerritory("Vizsla")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col> </Col>
        <Col>
          <TerritoryView
            key="Chihuahua"
            territory={getTerritory("Chihuahua")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col></Col>
      </Row>
      <br />
      <Row>
        <Col md={1}>
          <TerritoryView
            key="Akita"
            territory={getTerritory("Akita")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col md={1}> </Col>
        <Col md={2}>
          <TerritoryView
            key="Sheepdog"
            territory={getTerritory("Sheepdog")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col md={3}> </Col>
        <Col md={2}>
          <TerritoryView
            key="Maltese"
            territory={getTerritory("Maltese")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
        <Col md={1}> </Col>
        <Col md={2}>
          <TerritoryView
            key="Collie"
            territory={getTerritory("Collie")}
            handleSourceOrTarget={props.handleSourceOrTarget}
            player={player}
          />
        </Col>
      </Row>
    </Container>
  );
};

export default Map3Players;
