import React, { useState } from "react";
import TerritoryDetailView from "./TerritoryDetailView";
import TerritoryBasicView from "./TerritoryBasicView";
import Container from "react-bootstrap/Container";
import Modal from "react-bootstrap/Modal";

const TerritoryView = (props) => {
  const { player, territory } = props;
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const getStyles = () => {
    const styles = {
      //backgroundColor: getTerritoryColor(territory.owner.name),
      backgroundColor: getTerritoryColor(findView().ownerDisplay.name),
      cursor: "pointer",
    };
    const displayType = findView().displayType;
    if (displayType === "INVISIBLE") {
      styles.backgroundColor = "#77A6F7";
      //styles.pointerEvents = "none";
    } else if (displayType === "VISIBLE_OLD") {
      styles.filter = "grayscale(70%)";
    }
    return styles;
  };

  const findView = () => {
    return territory.territoryViews.find(
      (view) => view.viewer.name === player.name
    );
  };

  const findSpyUnit = () => {
    return territory.spyUnits.find(
        (spyUnit) => spyUnit.owner.name === player.name
    );
  };

  const isOwner = territory.owner.name === player.name;

  return (
    <>
      <div
        key={territory.name}
        onClick={findView().displayType === "INVISIBLE"? null: handleShow}
        className="territory"
        style={getStyles()}
      >
        <Container>
          <TerritoryBasicView
            handleSourceOrTarget={props.handleSourceOrTarget}
            handleClose={handleClose}
            territory={territory}
            player={player}
            view={findView()}
          />
        </Container>
      </div>

      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>
            {territory.name} owned by{" "}
            {isOwner ? territory.owner.name : findView().ownerDisplay.name}
            {findView().displayType === "VISIBLE_OLD" ? " (OLD INFO!)" : ""}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <TerritoryDetailView view={findView()} spyUnit={findSpyUnit()}/>
        </Modal.Body>
      </Modal>
    </>
  );
};

const getTerritoryColor = (owner) => {
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

export default TerritoryView;
