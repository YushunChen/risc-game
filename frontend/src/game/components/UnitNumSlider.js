import React from "react";
import "rc-slider/assets/index.css";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";

const UnitNumSlider = (props) => {
    const unitType = props.unitType;
    const unitNum = props.unitNum;
    const sliderValue = props.sliderValue;
    const handleSliderChange = props.handleSliderChange;

    const rowStyles = {
        height: "3rem",
    };

    return (
        <Row className="text-center" style={rowStyles}>
            <Col md={4}>{unitType}</Col>
            <Col md={8}>
                <div>
                    <input type="range" min={0} max={unitNum} value={sliderValue} onChange={handleSliderChange} />
                    <p>number of units: {sliderValue}</p>
                </div>
            </Col>
        </Row>
    );
};

export default UnitNumSlider;