import Modal from "react-bootstrap/Modal";
import UnitNumSlider from "./UnitNumSlider";
import { Button } from "react-bootstrap";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useContext } from "react";
import { OrderContext } from "../context/OrderProvider";

const UnitSelectModal = (props) => {
    const { addManyOrders } = useContext(OrderContext);

    const territories = props.territories;
    const sourceName = props.source;
    const targetName = props.target;
    const orderType = props.orderType;

    const getTerritory = (name) => {
        return territories.find((territory) => territory.name === name);
    };

    const [sliderValue0, setSliderValue0] = useState(0);
    const [sliderValue1, setSliderValue1] = useState(0);
    const [sliderValue2, setSliderValue2] = useState(0);
    const [sliderValue3, setSliderValue3] = useState(0);
    const [sliderValue4, setSliderValue4] = useState(0);
    const [sliderValue5, setSliderValue5] = useState(0);
    const [sliderValue6, setSliderValue6] = useState(0);
    const [sliderValue7, setSliderValue7] = useState(0);

    const handleSlider0Change = (event) => {
        setSliderValue0(event.target.value);
    };
    const handleSlider1Change = (event) => {
        setSliderValue1(event.target.value);
    };
    const handleSlider2Change = (event) => {
        setSliderValue2(event.target.value);
    };
    const handleSlider3Change = (event) => {
        setSliderValue3(event.target.value);
    };
    const handleSlider4Change = (event) => {
        setSliderValue4(event.target.value);
    };
    const handleSlider5Change = (event) => {
        setSliderValue5(event.target.value);
    };
    const handleSlider6Change = (event) => {
        setSliderValue6(event.target.value);
    };
    const handleSlider7Change = (event) => {
        setSliderValue7(event.target.value);
    };

    const navigate = useNavigate();
    const [showModal, setShowModal] = useState(true);
    const closeModal = () => {
        setShowModal(false);
        navigate("/", { state: { gameId: props.gameId } });
    };

    const orderArray = []
    const packageData = (sliderValue, unitType) => {
        if (sliderValue !== 0) {
            const data = {
                sourceTerritoryId: getTerritory(sourceName).id,
                destinationTerritoryId: getTerritory(targetName).id,
                unitNum: sliderValue,
                unitType: unitType,
                orderType: props.orderType
            }
            orderArray.push(data);
        }
    };

    const handleConfirmOrder = (e) => {
        e.preventDefault();
        packageData(sliderValue0, "Basic");
        packageData(sliderValue1, "Infantry");
        packageData(sliderValue2, "Cavalry");
        packageData(sliderValue3, "Artillery");
        packageData(sliderValue4, "Army Aviation");
        packageData(sliderValue5, "Special Forces");
        packageData(sliderValue6, "Combat Engineer");
        if(orderType==="MOVE") packageData(sliderValue7, "SPY");

        console.log("orderArray: ", orderArray);
        // store the order in context
        addManyOrders(orderArray);
        setShowModal(false);
        navigate("/", { state: { gameId: props.gameId } });
    }

    const getTotalUnitsOnTerritory = (territory) => {
        let totalUnits = 0;
        territory.units.forEach(unit => {
            totalUnits += unit.unitNum;
        });
        return totalUnits;
    };

    const getTotalUnitsOnPlayer = () => {
        let totalUnits = 0;
        territories.forEach(territory => {
            if (territory.owner.name === props.player.name) {
                totalUnits += getTotalUnitsOnTerritory(territory);
            }
            if(orderType==="MOVE"){
                territory.spyUnits.forEach(spyUnit => {
                    if(spyUnit.owner.name === props.player.name) {
                        totalUnits += spyUnit.unitNum;
                    }
                })
            }
        });
        return totalUnits;
    }

    if (!sourceName || !targetName) return;

    return (
        <>
            <Modal show={showModal}>
                <Modal.Header>
                    <Modal.Title>
                        Select units to {props.orderType} from {sourceName} to {targetName}
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div>
                        <UnitNumSlider unitType="Basic" unitNum={getTotalUnitsOnPlayer()} sliderValue={sliderValue0} handleSliderChange={handleSlider0Change} />
                        <UnitNumSlider unitType="Infantry" unitNum={getTotalUnitsOnPlayer()} sliderValue={sliderValue1} handleSliderChange={handleSlider1Change} />
                        <UnitNumSlider unitType="Cavalry" unitNum={getTotalUnitsOnPlayer()} sliderValue={sliderValue2} handleSliderChange={handleSlider2Change} />
                        <UnitNumSlider unitType="Artillery" unitNum={getTotalUnitsOnPlayer()} sliderValue={sliderValue3} handleSliderChange={handleSlider3Change} />
                        <UnitNumSlider unitType="Army Aviation" unitNum={getTotalUnitsOnPlayer()} sliderValue={sliderValue4} handleSliderChange={handleSlider4Change} />
                        <UnitNumSlider unitType="Special Forces" unitNum={getTotalUnitsOnPlayer()} sliderValue={sliderValue5} handleSliderChange={handleSlider5Change} />
                        <UnitNumSlider unitType="Combat Engineer" unitNum={getTotalUnitsOnPlayer()} sliderValue={sliderValue6} handleSliderChange={handleSlider6Change} />
                        {orderType === "MOVE" && (<UnitNumSlider unitType="SPY" unitNum={getTotalUnitsOnPlayer()} sliderValue={sliderValue7} handleSliderChange={handleSlider7Change} />)}
                        <br />
                        <div style={{ display: "flex", justifyContent: "center" }}>
                            <Button onClick={handleConfirmOrder} style={confirmButtonStyles} size="lg">Confirm</Button>
                            <Button onClick={closeModal} style={cancelButtonStyles} size="lg">Cancel</Button>
                        </div>
                    </div>
                </Modal.Body>
            </Modal>
        </>
    );
};

const confirmButtonStyles = {
    backgroundColor: "#26BC26", marginRight: "50px", border: "none"
}

const cancelButtonStyles = {
    ...confirmButtonStyles,
    backgroundColor: "#D33431"
}

export default UnitSelectModal;