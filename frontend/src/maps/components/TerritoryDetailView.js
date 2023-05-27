import React from "react";
import { GiSwordman, GiSwordwoman, GiBowman, GiCaveman, GiPikeman, GiStrongMan, GiPyromaniac } from "react-icons/gi";
import { Col, Row } from "react-bootstrap";

const TerritoryDetailView = (props) => {
  const { view, spyUnit} = props;
  const units = view.unitsDisplay;
  const iconSize = 35;
  const rowStyles = {
    height: "3rem",
  };
  const getUnitIcon = (unitType) => {
    switch (unitType) {
      case "LEVEL0":
        return <GiSwordman size={iconSize} />;
      case "LEVEL1":
        return <GiSwordwoman size={iconSize} />;
      case "LEVEL2":
        return <GiBowman size={iconSize} />;
      case "LEVEL3":
        return <GiPikeman size={iconSize} />;
      case "LEVEL4":
        return <GiCaveman size={iconSize} />;
      case "LEVEL5":
        return <GiStrongMan size={iconSize} />;
      case "LEVEL6":
        return <GiPyromaniac size={iconSize} />;
      default:
        return <GiSwordman size={iconSize} />;
    }
  };
  const getUnitTypeName = (unitType) => {
    switch (unitType) {
      case "LEVEL0":
        return "Basic";
      case "LEVEL1":
        return "Infantry";
      case "LEVEL2":
        return "Cavalry";
      case "LEVEL3":
        return "Artillery";
      case "LEVEL4":
        return "Army Aviation";
      case "LEVEL5":
        return "Special Forces";
      case "LEVEL6":
        return "Combat Engineer";
      default:
        return "Basic";
    }
  }
  return (
    <div>
      {units.map((unit) => {
        return (
          <Row key={unit.unitType} className="text-center" style={rowStyles}>
            <Col md={4}>
              {getUnitIcon(unit.unitType)}
            </Col>
            <Col md={4}>{getUnitTypeName(unit.unitType)}</Col>
            <Col md={4}>{unit.unitNum}</Col>
          </Row>
        )
      })}
      <Row key={"SPY"} className="text-center" style={rowStyles}>
        <Col md={4}>
          {getUnitIcon("SPY")}
        </Col>
        <Col md={4}>{"SPY"}</Col>
        <Col md={4}>{spyUnit.unitNum}</Col>
      </Row>
    </div>
  );
};

export default TerritoryDetailView;
