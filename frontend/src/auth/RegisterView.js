import React, { useState } from "react";
import { Card, Form, Button, Container } from "react-bootstrap";
import { Link } from 'react-router-dom';
import axios from "axios";
import Alert from 'react-bootstrap/Alert';
import { useNavigate } from "react-router-dom";

const RegisterView = () => {
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showAlert, setShowAlert] = useState(false);
  const [showSuccess, setShowSuccess] = useState(false);
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post(`register`, {
        fullName: fullName,
        email: email,
        password: password,
      });
      console.log(response.data);
      setShowSuccess(true);
      navigate('/login');
    } catch (error) {
      console.log(error);
      setShowAlert(true);
      setFullName('');
      setEmail('');
      setPassword('');
    }
  }


  return (
    <>{showAlert &&
      <Alert variant="danger" onClose={() => setShowAlert(false)} dismissible>
        <Alert.Heading>Oh snap! You got an error!</Alert.Heading>
        <p>
          Please try again with another email.
        </p>
      </Alert>
    }
    {showSuccess &&
      <Alert variant="success" onClose={() => setShowSuccess(false)} dismissible>
        <Alert.Heading>Yes! You are in!</Alert.Heading>
        <p>
          Welcome to the RISC game!
        </p>
      </Alert>
    }
      <Container className="vh-100 d-flex align-items-center justify-content-center">
        <Card style={cardStyles}>
          <Card.Body>
            <Card.Title className="text-center" style={titleStyles}>
              Create Account
            </Card.Title>
            <Form onSubmit={handleRegister}>
              <Form.Group controlId="formFullName">
                <Form.Label style={textStyles}>Full Name</Form.Label>
                <Form.Control
                  type="text"
                  style={{ backgroundColor: "#e9f0fd" }}
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                />
              </Form.Group>

              <Form.Group controlId="formEmail">
                <Form.Label style={textStyles}>Email</Form.Label>
                <Form.Control
                  type="email"
                  style={{ backgroundColor: "#e9f0fd" }}
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              </Form.Group>

              <Form.Group controlId="formPassword">
                <Form.Label style={textStyles}>Password</Form.Label>
                <Form.Control
                  type="password"
                  style={{ backgroundColor: "#e9f0fd" }}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </Form.Group>
              <div className="text-center" style={{ marginTop: "5%" }} >
                <Button variant="primary" style={buttonStyles} size="lg" type="submit" block="true">
                  Register
                </Button>
              </div>
            </Form>
            <p className="mt-3 mb-0 text-center" style={textStyles}>Already have an account?{' '}
              <Link to="/login" style={{ color: "#77A6F7" }}>Log in</Link> now.</p>
          </Card.Body>
        </Card>
      </Container >
    </>
  );
};

const cardStyles = {
  width: "30rem",
  height: "25rem",
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

export default RegisterView;
