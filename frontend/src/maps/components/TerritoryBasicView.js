import React from "react";
import { GiFruitBowl } from "react-icons/gi";
import { HiOutlineDesktopComputer } from "react-icons/hi";
import { GiSwordman } from "react-icons/gi";
import { Col, Row } from "react-bootstrap";

const TerritoryBasicView = (props) => {
  const { territory, view, player } = props;
  const getTotalUnits = () => {
    let totalUnits = 0;
    view.unitsDisplay.forEach((unit) => {
      totalUnits += unit.unitNum;
    });
    return totalUnits;
  };

  const handleClick = (e) => {
    // stop propagation if attacking
    if (props.handleSourceOrTarget) {
      e.stopPropagation();
    } else {
      return;
    }
    console.log(e.target.innerText);
    if(e.target.innerText === "Invisible") props.handleSourceOrTarget(territory.name)
    else props.handleSourceOrTarget(e.target.innerText);
  };
  const isOwner = territory.owner.name === player.name;
  if (view.displayType === "INVISIBLE") return (
      <div
          className="text-center territory-name"
          onClick={handleClick}
          style={{ margin: "0.8rem", fontSize: "18px", color: "#379EBF"}}
      >
        Invisible
      </div>
  );

  return (
    <div style={{ fontSize: "12px" }}>
      <div
        className="text-center territory-name"
        onClick={handleClick}
        style={{ margin: "0.8rem", fontSize: "18px" }}
      >
        {territory.name}
      </div>

      <Row className="text-center">
        <Col md={4}>
          <GiSwordman size={20} />
        </Col>
        <Col md={4}>
          <GiFruitBowl size={20} />
        </Col>
        <Col md={4}>
          <HiOutlineDesktopComputer size={20} />
        </Col>
      </Row>
      <Row className="text-center">
        <Col md={4}>
          <p style={resourceNumStyles}>{getTotalUnits()}</p>
        </Col>
        <Col md={4}>
          <p style={resourceNumStyles}>+{territory.foodProduction}</p>
        </Col>
        <Col md={4}>
          <p style={resourceNumStyles}>+{territory.techProduction}</p>
        </Col>
      </Row>
    </div>
  );
};

const resourceNumStyles = {
  fontSize: "9px",
};
export default TerritoryBasicView;
